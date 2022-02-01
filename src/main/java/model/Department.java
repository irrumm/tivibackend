package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    private int seatsInCouncil;

    private List<Candidate> council = new ArrayList<>();
    private List<Candidate> qualifiedCandidates = new ArrayList<>();
    private List<Candidate> unqualifiedCandidates = new ArrayList<>();

    public Department(int seatsInCouncil) {
        this.seatsInCouncil = seatsInCouncil;
    }

    /**
     * Adds a candidate to the department
     * If candidate has 0 votes, they are added to the unqualified candidate list
     * If candidate has at least 1 vote, they are added to the qualified candidate list
     * @param candidate Candidate that is being added
     */
    public void addCandidate(Candidate candidate) {
        // Check that Candidate is not in any list
        if (!qualifiedCandidates.contains(candidate) && !unqualifiedCandidates.contains(candidate) && !council.contains(candidate)) {
            // if Candidate has 0 votes, add them to unqualified candidates list; otherwise add them to qualified candidates list
            if (candidate.getVoteCount() == 0) {
                unqualifiedCandidates.add(candidate);
            } else {
                for (var c: qualifiedCandidates) {
                    if (c.getVoteCount() == candidate.getVoteCount()) {
                        c.setTied(true);
                        candidate.setTied(true);
                    }
                }
                qualifiedCandidates.add(candidate);
            }
        }
    }

    /**
     * Orders qualified candidates by vote count (descending)
     */
    public void orderQualifiedCandidates()  {
        qualifiedCandidates.sort(Comparator.comparingInt(Candidate::getVoteCount).reversed());
    }

    /**
     * Orders unqualified candidates by name
     */
    public void orderUnqualifiedCandidates() {
        unqualifiedCandidates.sort(Comparator.comparing(Candidate::getName));
    }

    /**
     * Adds candidates to council and removes them from qualified candidate list
     */
    public void addCandidatesToCouncil() {
        // Candidates that are added to council are removed from qualified candidates' list
        List<Candidate> removedCandidates = new ArrayList<>();

        for (var candidate: qualifiedCandidates) {
            // if council is full then end the loop;
            if (isCouncilFull()) {
                break;
            }
            // if team does not have a representative then add them to council
            if (!teamHasRepresentative(candidate.getTeam())) {
                council.add(candidate);
                removedCandidates.add(candidate);
            }
        }
        qualifiedCandidates.removeAll(removedCandidates);
    }

    /**
     * Fills remaining empty seats in council
     */
    public void fillEmptySeats() {
        List<Candidate> removedCandidates = new ArrayList<>();

        for (var candidate: qualifiedCandidates) {
            // if council is full then end the loop;
            if (isCouncilFull()) {
                break;
            }
            council.add(candidate);
            removedCandidates.add(candidate);
        }
        qualifiedCandidates.removeAll(removedCandidates);
    }

    /**
     * Checks if team has a representative in the council
     * @param team Team name
     */
    private boolean teamHasRepresentative(String team) {
        return council.stream().anyMatch(candidate -> candidate.getTeam().equals(team));
    }

    /**
     * Checks if all the seats in the council have been filled
     */
    private boolean isCouncilFull() {
        return council.size() == seatsInCouncil;
    }
}
