package com.blackfox.jpmorgan.entity;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Report {
    private LocalDate settlementDate;
    private Map<String, Float> outgoing;
    private Map<String, Float> incoming;

    public Report(float outgoing, float incoming, LocalDate settlementDate, String entity, boolean buy) {
        this.settlementDate = settlementDate;

        this.outgoing = new TreeMap<>();
        this.incoming = new TreeMap<>();

        if (buy) {
            this.outgoing.put(entity, outgoing);
        } else {
            this.incoming.put(entity, incoming);
        }

    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void addAllIncoming(Map<String, Float> incomingToAdd) {
        incomingToAdd.forEach((key, value) -> {
            if (incoming.containsKey(key)) {
                incoming.put(key, incoming.get(key) + value);
            } else {
                incoming.put(key, value);
            }
        });
    }

    public void addAllOutgoing(Map<String, Float> outgoingToAdd) {
        outgoingToAdd.forEach((key, value) -> {
            if (outgoing.containsKey(key)) {
                outgoing.put(key, outgoing.get(key) + value);
            } else {
                outgoing.put(key, value);
            }
        });
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public Map<String, Float> getOutgoing() {
        return outgoing;
    }

    public Map<String, Float> getIncoming() {
        return incoming;
    }

    @Override
    public String toString() {
        String outgoingRanking = outgoing.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));

        String incomingRanking = incoming.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));

        return "Settlement Date: " + settlementDate +
                "\nAmount in USD settled incoming: " + incoming.values().stream().reduce(0F, Float::sum) +
                "\nAmount in USD settled outgoing: " + outgoing.values().stream().reduce(0F, Float::sum) +
                "\nRanking of entities for incoming: " + incomingRanking +
                "\nRanking of entities for outgoing: " + outgoingRanking + "\n\n";
    }
}
