package com.aisvalidator.analyzer;

import com.aisvalidator.analyzer.service.utils.AnalysisUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AnalyzerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void binningDeterminationTest() {
		double lonMin = 0;
		double lonMax = 1;
		int lonBinNumber = 100;
		Assertions.assertEquals(99, AnalysisUtils.determineBin(lonMin, lonMax, lonBinNumber, 1));
		Assertions.assertEquals(0, AnalysisUtils.determineBin(lonMin, lonMax, lonBinNumber, 0));
		Assertions.assertNull(AnalysisUtils.determineBin(lonMin, lonMax, lonBinNumber, -1));
		Assertions.assertNull(AnalysisUtils.determineBin(lonMin, lonMax, lonBinNumber, 1.000001));
	}

	@Test
	void calculateRelativeError() {
		Assertions.assertEquals(100, AnalysisUtils.calculateRelativeError(100, 200));
		Assertions.assertEquals(0, AnalysisUtils.calculateRelativeError(0, 0));
		Assertions.assertEquals(50, AnalysisUtils.calculateRelativeError(10, 5));
		Assertions.assertEquals(11, AnalysisUtils.calculateRelativeError(9, 10));
	}

}
