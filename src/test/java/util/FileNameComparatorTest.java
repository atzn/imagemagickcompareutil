package util;

import java.io.File;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class FileNameComparatorTest {
	@Test
	public void testFileNameComparator() {
		FileNameComparator fileNameComparator = new FileNameComparator();
		File[] files = new File[]{new File("XX"), new File("AA"), new File("ZZ"), new File("BB"), new File("AB"), new File("XY1")};
		Arrays.sort(files, fileNameComparator);
		Assert.assertEquals("AA", files[0].getName());
		Assert.assertEquals("AB", files[1].getName());
		Assert.assertEquals("BB", files[2].getName());
		Assert.assertEquals("XX", files[3].getName());
		Assert.assertEquals("XY1", files[4].getName());
		Assert.assertEquals("ZZ", files[5].getName());
	}
}
