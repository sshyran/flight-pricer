package io.yac.flight.pricer.model;


import java.util.ArrayList;
import java.util.List;

public class Slice {
    private List<Segment> segments;

    public Slice() {
        this.segments = new ArrayList<>();
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
}
