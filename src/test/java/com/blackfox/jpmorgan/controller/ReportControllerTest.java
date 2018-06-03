package com.blackfox.jpmorgan.controller;

import com.blackfox.jpmorgan.entity.Instruction;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportControllerTest {


    @Test
    public void testReport() {
        // setup:
        List<Instruction> instructions = createMockInstructions();

        // when:
        String report = new ReportController().createReport(instructions);

        // then:
        Assert.assertEquals("Settlement Date: 2016-05-09\n" +
                "Amount in USD settled incoming: 31000.0\n" +
                "Amount in USD settled outgoing: 0.0\n" +
                "Ranking of entities for incoming: BBVA\n" +
                "Ranking of entities for outgoing: \n" +
                "\n" +
                "Settlement Date: 2016-05-08\n" +
                "Amount in USD settled incoming: 6000.0\n" +
                "Amount in USD settled outgoing: 0.0\n" +
                "Ranking of entities for incoming: bar\n" +
                "Ranking of entities for outgoing: \n" +
                "\n" +
                "Settlement Date: 2016-01-07\n" +
                "Amount in USD settled incoming: 14899.5\n" +
                "Amount in USD settled outgoing: 28202.25\n" +
                "Ranking of entities for incoming: bar\n" +
                "Ranking of entities for outgoing: BCRA, Rio Bank\n" +
                "\n" +
                "Settlement Date: 2016-01-04\n" +
                "Amount in USD settled incoming: 0.0\n" +
                "Amount in USD settled outgoing: 10025.0\n" +
                "Ranking of entities for incoming: \n" +
                "Ranking of entities for outgoing: foo\n\n", report);
    }

    private List<Instruction> createMockInstructions() {
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(new Instruction (
                "foo",
                "B",
                0.50f,
                "SPG",
                LocalDate.of(2016, 01, 01),
                LocalDate.of(2016, 01, 02),
                200,
                100.25f
        ));

        instructions.add(new Instruction (
                "bar",
                "S",
                0.22f,
                "AED",
                LocalDate.of(2016, 01, 05),
                LocalDate.of(2016, 01, 07),
                450,
                150.5f
        ));

        instructions.add(new Instruction (
                "Rio Bank",
                "B",
                0.22f,
                "AED",
                LocalDate.of(2016, 01, 05),
                LocalDate.of(2016, 01, 07),
                225,
                145.5f
        ));

        instructions.add(new Instruction (
                "BCRA",
                "B",
                0.60f,
                "AED",
                LocalDate.of(2016, 01, 05),
                LocalDate.of(2016, 01, 07),
                250,
                140.0f
        ));

        instructions.add(new Instruction (
                "bar",
                "S",
                0.15f,
                "AED",
                LocalDate.of(2016, 05, 05),
                LocalDate.of(2016, 05, 07),
                200,
                200.0f
        ));

        instructions.add(new Instruction (
                "BBVA",
                "S",
                0.62f,
                "PEA",
                LocalDate.of(2016, 05, 05),
                LocalDate.of(2016, 05, 07),
                125,
                400.0f
        ));

        return instructions;
    }
}
