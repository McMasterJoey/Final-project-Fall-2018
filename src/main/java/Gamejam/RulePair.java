package Gamejam;

import java.io.Serializable;
import java.util.HashSet;
/**
 * Houses a single rule for the Dynamic Theme object.
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class RulePair {
	
	private HashSet<Integer> statements;
	private ThemePair pair;
	private boolean useAnd;
	public RulePair(HashSet<Integer> statements, ThemePair pair, boolean useAnd)
	{
		this.statements = statements;
		this.pair = pair;
		this.useAnd = useAnd;
	}
	public RulePair(RulePairSerializable data)
	{
		this.statements = data.getStatements();
		this.useAnd = data.getUseAnd();
		this.pair = new ThemePair(data.getThemePair());
	}
	public ThemePair getPair()
	{
		return this.pair;
	}
	public HashSet<Integer> getStatements()
	{
		return this.statements;
	}
	public boolean getAnd()
	{
		return this.useAnd;
	}
	public RulePairSerializable dumpCoreData()
	{
		RulePairSerializable retval = new RulePairSerializable(this.statements,pair.dumpCoreData(),this.useAnd);
		return retval;
	}
}