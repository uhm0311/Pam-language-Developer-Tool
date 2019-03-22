package bytecodeComponent.util;


public class LineNumberFrame {

	private int StartPc;
	private int LineNumber;
	
	public LineNumberFrame(int StartPc, int LineNumber)
	{
		this.StartPc = StartPc;
		this.LineNumber = LineNumber;
	}
	public void setStartPc(int StartPc)
	{
		this.StartPc = StartPc;
	}
	public int getStartPc()
	{
		return StartPc;
	}
	public void setLineNumber(int LineNumber)
	{
		this.LineNumber = LineNumber;
	}
	public int getLineNumber()
	{
		return LineNumber;
	}
	public String toString()
	{
		String str = "";
		
		str += Util.intToHex((StartPc), 4);
		str += Util.intToHex((LineNumber), 4);
		
		return str;
	}
}
