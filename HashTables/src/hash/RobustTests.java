package hash;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RobustChainedHashMapTest.class,
		RobustLinearProbedHashMapTest.class,
		RobustQuadraticProbedHashMapTest.class })
public class RobustTests {

}
