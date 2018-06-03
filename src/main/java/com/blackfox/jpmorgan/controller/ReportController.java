package com.blackfox.jpmorgan.controller;

import com.blackfox.jpmorgan.entity.Instruction;
import com.blackfox.jpmorgan.entity.Report;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class ReportController {
    private static final String BUYING = "B";
    private static final List<String> CURRENCIES = Arrays.asList("SGP", "AED");

    /**
     * Method to create daily report from instructions
     * @param instructions to perform
     * @return the output of the operation
     */
    String createReport(List<Instruction> instructions) {

        // 1 - Get reports out of instructions
        List<Report> reports = instructions.stream().map(instruction -> {
            if (BUYING.equals(instruction.getOperation())) {
                return new Report((instruction.getPricePerUnit() * instruction.getUnits() * instruction.getAgreedFx()),
                        0,
                        ckeckSettlementDate(instruction.getSettlementDate(), instruction.getCurrency()),
                        instruction.getEntity(), true);
            } else {
                return new Report(0,
                        (instruction.getPricePerUnit() * instruction.getUnits() * instruction.getAgreedFx()),
                        ckeckSettlementDate(instruction.getSettlementDate(), instruction.getCurrency()),
                        instruction.getEntity(), false);
            }
        }).collect(Collectors.toList());

        // 2 - Group them by settlement date
        Map<LocalDate, Optional<Report>> reportsByDay = reports.stream()
                .collect(Collectors.groupingBy
                        (Report::getSettlementDate,
                                Collectors.reducing((Report a, Report b) -> {
                                    a.setSettlementDate(b.getSettlementDate());
                                    a.addAllIncoming(b.getIncoming());
                                    a.addAllOutgoing(b.getOutgoing());
                                    return a;
                                })));

        return reportsByDay.values().stream().map(Optional::get).map(Report::toString).collect(Collectors.joining(""));
    }

    private static LocalDate ckeckSettlementDate(LocalDate settlementDate, String currency) {
        if (CURRENCIES.contains(currency)) {
            while (DayOfWeek.FRIDAY.equals(settlementDate.getDayOfWeek())
                    || DayOfWeek.SATURDAY.equals(settlementDate.getDayOfWeek())) {
                settlementDate = settlementDate.plusDays(1);
            }
        } else {
            while (DayOfWeek.SATURDAY.equals(settlementDate.getDayOfWeek()) || DayOfWeek.SUNDAY.equals(settlementDate.getDayOfWeek())) {
                settlementDate = settlementDate.plusDays(1);
            }
        }
        return settlementDate;
    }
}
