package ann;

public class NaiveTraining<E extends Number> implements TrainingAlgorithm<E> {

	@Override
	public void adjustWeights(MatrixANN<E> ann, E[] correctOutput) {
		ann.processLayers();
		double prevErr = ann.error(correctOutput, ann.getOutput());
		System.out.println("correctOutput " + correctOutput[0]);
		for (int w = 0; w < ann.getWeights().length; w++) { // loop through each weight matrix in ann
			for (int r = 0; r < ann.getWeights(w).numRows(); r++) { // loop through each weight in weight matrix
				for (int c = 0; c < ann.getWeights(w).numCols(); c++) {
					System.out.println("Before increase weight: " + ann.getOutput()[0]);
					ann.getWeights(w).addTo(r, c, ann.getTestStepSize());
					ann.processLayers();
					double err = ann.error(correctOutput, ann.getOutput());
					System.out.println("After increase weight: " + ann.getOutput()[0]);
					ann.getWeights(w).addTo(r, c, -ann.getTestStepSize());
					if (ann.getSimultaneousChanges()) {
						if (err < prevErr)
							ann.getWeightChanges(w).set(r, c, ann.stepSize(err, prevErr));
						else
							ann.getWeightChanges(w).set(r, c, -ann.stepSize(err, prevErr));
					} else {
						if (err < prevErr)
							ann.getWeights(w).addTo(r, c, ann.stepSize(err, prevErr));
						else
							ann.getWeights(w).addTo(r, c, -ann.stepSize(err, prevErr));
						prevErr = err;
					}
				}
			}
		}
	}

	@Override
	public void adjustBiases(MatrixANN<E> ann, E[] correctOutput) {
		ann.processLayers();
		double prevErr = ann.error(correctOutput, ann.getOutput());
		for (int b = 0; b < ann.getBiases().length; b++) {
			ann.setBias(b, (E)(Number)(ann.getBias(b).doubleValue() + ann.getTestStepSize()));
			ann.processLayers();
			ann.setBias(b, (E)(Number)(ann.getBias(b).doubleValue() - ann.getTestStepSize()));
			double err = ann.error(correctOutput, ann.getOutput());
			if (ann.getSimultaneousChanges()) {
				if (err < prevErr)
					ann.setBiasChange(b, (E)(Number)ann.stepSize(err, prevErr));
				else
					ann.setBiasChange(b, (E)(Number)(-ann.stepSize(err, prevErr)));
			} else {
				if (err < prevErr)
					ann.setBias(b, (E)(Number)(ann.getBias(b).doubleValue() + ann.stepSize(err, prevErr)));
				else
					ann.setBias(b, (E)(Number)(ann.getBias(b).doubleValue() - ann.stepSize(err, prevErr)));
				prevErr = err;
			}
		}
	}
	
	/** 
	 * Make all marked changes
	 */
	public void commitChanges(MatrixANN<E> ann) {
		if (!ann.getSimultaneousChanges())
			return;
		for (int w = 0; w < ann.getWeights().length; w++) { // loop through each weight matrix in ann
			for (int r = 0; r < ann.getWeights(w).numRows(); r++) { // loop through each weight in weight matrix
				for (int c = 0; c < ann.getWeights(w).numCols(); c++) {
					ann.getWeights(w).addTo(r, c, ann.getWeightChanges(w).get(r, c));
				}
			}
		}
		for (int b = 0; b < ann.getBiases().length; b++) {
			ann.setBias(b, (E)(Number)(ann.getBias(b).doubleValue() + ann.getBiasChange(b).doubleValue()));
		}
	}

	@Override
	public void trainOne(MatrixANN<E> ann, E[] input, E[] correctOutput) {
		ann.setInputs(input);
		adjustWeights(ann, correctOutput);
		adjustBiases(ann, correctOutput);
		commitChanges(ann);		
	}

	@Override
	public void trainBatch(MatrixANN<E> ann, E[][] inputBatch, E[][] outputLabels) {
		
	}

}
