package io.yac.flight.pricer.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Slice {
    private final String id;

    private List<Segment> segments;
    private Integer duration;

    public Slice() {
        this.segments = new ArrayList<>();
        id = UUID.randomUUID().toString();
    }

    public void addSegment(Segment segment) {
        segments.add(segment);
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public String getId() {
        return id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Set<String> getAirlinesIATA() {
        return segments.stream().map(Segment::getAirline).collect(Collectors.toSet());
    }
}
