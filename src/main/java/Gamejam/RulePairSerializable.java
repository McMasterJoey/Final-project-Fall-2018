package Gamejam;

import java.io.Serializable;
import java.util.HashSet;

/**
 * A Rulepair that is formated to be serializable. 
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class RulePairSerializable implements Serializable
{
	private static final long serialVersionUID = 1;
	private HashSet<Integer> statements;
	private ThemePairSerializable pair;
	private boolean useAnd;
	public RulePairSerializable(HashSet<Integer> statements, ThemePairSerializable pair, boolean useAnd)
	{
		this.statements = statements;
		this.pair = pair;
		this.useAnd = useAnd;
	}
	public HashSet<Integer> getStatements()
	{
		return this.statements;
	}
	public ThemePairSerializable getThemePair()
	{
		return this.pair;
	}
	public boolean getUseAnd()
	{
		return this.useAnd;
	}
}
