package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.service.MatchResultProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MatchResultProcessorFactory {
    private final Map<TournamentType, MatchResultProcessor> processors;

    public MatchResultProcessorFactory(List<MatchResultProcessor> processorList) {
        this.processors = processorList.stream()
                .collect(Collectors.toMap(MatchResultProcessor::getType, processor -> processor));
    }

    public MatchResultProcessor getProcessor(TournamentType type){
        MatchResultProcessor processor=processors.get(type);
        if(processor==null){
            throw new ResourceNotFoundException("Processor not found");
        }
        return processor;
    }
}
