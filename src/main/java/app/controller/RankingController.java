package app.controller;

import model.Candidate;
import model.Department;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@CrossOrigin
@RestController
@RequestMapping("")
public class RankingController {

    public Department department = null;

    /**
     * Gets the department information as a byte array
     * @param data Department information
     * @return Created department
     */
    @PostMapping("")
    public Department getCandidateInfo(@RequestBody byte[] data) {
        department = createDepartment(data);
        department.orderQualifiedCandidates();
        department.orderUnqualifiedCandidates();
        return department;
    }

    /**
     * Overwrites the department according to tied candidates' appointed position
     * @param dep Rearranged department
     * @return Rearranged department
     */
    @PostMapping("rearrange")
    public Department rearrangeCandidates(@RequestBody Department dep) {
        department = dep;
        return department;
    }

    /**
     * Chooses the council members
     * @return Department with council members
     */
    @GetMapping("choose-council")
    public Department addToCouncil() {
        department.addCandidatesToCouncil();
        department.fillEmptySeats();
        return department;
    }

    /**
     * Returns the department
     */
    @GetMapping("")
    public Department getDepartment() {
        return department;
    }

    /**
     * Creates the department from byte array
     * @param data Department information
     * @return Created department
     */
    private static Department createDepartment(byte[] data) {
        // Convert byte array to string
        String candidateString = new String(data, StandardCharsets.UTF_8);
        String[] rankingInput = candidateString.split("\\r?\\n");
        // First row contains number of teams and number of seats in council
        String[] header = rankingInput[0].split("\t");

        Department department = new Department(Integer.parseInt(header[1]));

        // Remaining rows contain candidate name, team name and vote count
        String[] candidateInfo = Arrays.copyOfRange(rankingInput, 1, rankingInput.length);
        for (var candidate: candidateInfo) {
            String[] row = candidate.split("\t");
            department.addCandidate(new Candidate(row[0], row[1], Integer.parseInt(row[2]), false));
        }
        return department;
    }
}
