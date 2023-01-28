package com.crio.qcontest.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.crio.qcontest.entities.Contest;
import com.crio.qcontest.entities.ContestStatus;
import com.crio.qcontest.entities.Contestant;
import com.crio.qcontest.entities.Level;
import com.crio.qcontest.entities.Question;
import com.crio.qcontest.entities.User;
import com.crio.qcontest.repositories.IContestRepository;
import com.crio.qcontest.repositories.IContestantRepository;
import com.crio.qcontest.repositories.IQuestionRepository;
import com.crio.qcontest.repositories.IUserRepository;

public class ContestService {
    private final IContestantRepository contestantRepository;
    private final IContestRepository contestRepository;
    private final IQuestionRepository questionRepository;
    private final IUserRepository userRepository;

    public ContestService(IContestantRepository contestantRepository, IContestRepository contestRepository,
            IQuestionRepository questionRepository, IUserRepository userRepository) {
        this.contestantRepository = contestantRepository;
        this.contestRepository = contestRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    public Contest createContest(String title, Level level, String createdBy, Integer numQuestions) {
        // Check if creator exists.
        User creator = userRepository.findByName(createdBy)
                .orElseThrow(() -> new RuntimeException("User with name: " + createdBy + " not found!"));
        // Get total count of questions in repository.
        Integer totalQuestionsCount = questionRepository.count();
        // Throw RunTime Exception if requested number of questions are not present in
        // the repository.
        if (totalQuestionsCount <= numQuestions) {
            throw new RuntimeException("Requested Number of questions: " + numQuestions + " cannot be fulfilled!");
        }
        // Get questions with specified level from the repository.
        List<Question> questionList = questionRepository.findAllQuestionLevelWise(level);
        // Pick up random requested number of questions from the question list fetch
        // from the repository.
        List<Question> randomList = pickRandomQuestions(questionList, numQuestions);
        // Create a new Contest Object.
        Contest c = new Contest(title, level, creator, randomList);
        // Save the contest in the repository.
        Contest savedContest = contestRepository.save(c);
        // Register the Contest Creator to this contest. ( This operation will fail if
        // it is not implemented yet.)
        createContestant(savedContest.getId(), createdBy);
        return savedContest;
    }

    // https://www.codespeedy.com/how-to-randomly-select-items-from-a-list-in-java/
    private List<Question> pickRandomQuestions(List<Question> questionList, Integer numQuestions) {
        Random rand = new Random(); // object of Random class.
        // temporary list to hold selected items.
        List<Question> tempList = new ArrayList<>();
        for (int i = 0; i < numQuestions; i++) {
            int randomIndex = rand.nextInt(questionList.size());
            // the loop check on repetition of elements
            while (tempList.contains(questionList.get(randomIndex))) {
                randomIndex = rand.nextInt(questionList.size());
            }
            tempList.add(questionList.get(randomIndex));
        }
        return tempList;
    }

    public List<Contest> getContests(Level level) {
        if (level == null) {
            return contestRepository.findAll();
        }
        return contestRepository.findAllContestLevelWise(level);
    }

    public Contestant createContestant(Long contestId, String userName) {

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest with " + contestId + " not found!"));
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new RuntimeException("User with " + userName + " not found!"));
        ContestStatus contestStatus = contest.getContestStatus();
        if (ContestStatus.ENDED.equals(contestStatus)) {
            throw new RuntimeException("Contest has already ended!");
        }
        Contestant contestant = new Contestant(user, contest);
        return contestantRepository.save(contestant);
    }

    public String deleteContestant(Long contestId, String userName) {
        // Validate if the user is registered in the contest already.
        Contestant contestant = contestantRepository.find(contestId, userName).orElseThrow(
                () -> new RuntimeException("Contestant with " + contestId + " and " + userName + " not found!"));
        ContestStatus contestStatus = contestant.getContest().getContestStatus();
        // Check if contest is valid as per the required conditions.
        if (ContestStatus.IN_PROGRESS.equals(contestStatus)) {
            throw new RuntimeException("Contest has already started!");
        }
        if (ContestStatus.ENDED.equals(contestStatus)) {
            throw new RuntimeException("Contest has already ended!");
        }
        // Delete the contestant from the repository.
        contestantRepository.delete(contestant);
        return "Contestant with name " + userName + " for contest " + contestId + " deleted!";
    }

    public List<Contestant> runContest(Long contestId, String createdBy) {
        // Check if contest is valid as per the required conditions.
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest: " + contestId + " not found!"));
        ContestStatus contestStatus = contest.getContestStatus();
        if (ContestStatus.IN_PROGRESS.equals(contestStatus)) {
            throw new RuntimeException("Contest has already started!");
        }
        if (ContestStatus.ENDED.equals(contestStatus)) {
            throw new RuntimeException("Contest has already ended!");
        }
        if (!contest.getCreator().getName().equals(createdBy)) {
            throw new RuntimeException("User " + createdBy + " is not a valid creator for contest " + contestId + " !");
        }

        Level contestLevel = contest.getLevel();

        // Get all the contestants who registered for the given contest.
        List<Contestant> contestantList = contestantRepository.findAllByContestId(contestId);
        List<Question> contestQuestionList = contest.getQuestions();
        int contestQuestionListSize = contestQuestionList.size();
        // For each contestant,
        contestantList.forEach((contestant) -> {
            // Select random questions from the contest which will be considered as solved
            // by the contestant.
            List<Question> solvedQuestionsList = pickRandomQuestions(contestQuestionList,
                    getRandomNumberInRange(0, contestQuestionListSize));
            // Store the solved questions in the contestant.
            solvedQuestionsList.forEach((question) -> {
                contestant.addQuestion(question);
            });
            User user = contestant.getUser();
            // Generate new totalScore for the user as per the recently solved questions.
            int newScore = contestant.getCurrentContestPoints() + user.getTotalScore() - contestLevel.getWeight();
            // Update the totalscore of the user.
            user.modifyScore(newScore);
        });
        // End the contest once scores are updated for all the users.
        contest.endContest();
        // Return the list of contestants in descending order( user with highest score
        // is first ) as per their score in the current contest.
        return contestantList.stream().sorted((c1, c2) -> c2.getCurrentContestPoints() - c1.getCurrentContestPoints())
                .collect(Collectors.toList());
    }

    // https://mkyong.com/java/java-generate-random-integers-in-a-range/
    private int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.ints(min, (max + 1)).findFirst().getAsInt();
    }

    public List<Contestant> contestHistory(Long contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest: " + contestId + " not found!"));
        ContestStatus contestStatus = contest.getContestStatus();
        if (!ContestStatus.ENDED.equals(contestStatus)) {
            throw new RuntimeException("Contest has already ended!");
        }

        return contestantRepository.findAllByContestId(contestId).stream()
                .sorted((c1, c2) -> c2.getCurrentContestPoints() - c1.getCurrentContestPoints())
                .collect(Collectors.toList());
    }
}
