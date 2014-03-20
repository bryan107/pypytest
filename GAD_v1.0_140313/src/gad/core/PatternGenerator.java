package gad.core;

import java.util.Map;

public interface PatternGenerator {
	public Map<Integer, Map<Integer, EstimatedVariancePCA>> getEstimation(Map<Integer, Double> reading);
}
