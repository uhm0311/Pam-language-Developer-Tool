package bytecodeComponent;

import java.util.ArrayList;

import bytecodeComponent.util.GeneralAttribute;
import bytecodeComponent.util.LineNumberFrame;
import bytecodeComponent.util.StackMapFrame;
import bytecodeComponent.util.StackMapFrame.StackMapType;
import bytecodeComponent.util.StackMapFrame.TypeInfo;
import bytecodeComponent.util.Util;

public class StackMapTable extends GeneralAttribute {

	private ArrayList<StackMapFrame> Frames = new ArrayList<StackMapFrame>();
	private LineNumberTable LineNumTable;
	private boolean hasInputStatement = false;
	
	public StackMapTable(LineNumberTable LineNumTable) 
	{ 
		this.LineNumTable = LineNumTable;
	}
	
	public StackMapFrame get(int index)
	{
		return Frames.get(index);
	}
	public void set(int index, StackMapFrame Frame)
	{
		Frames.set(index, Frame);
	}
	public int size()
	{
		return Frames.size();
	}
	public void add(StackMapFrame[] Frames)
	{
		for(int i = 0; i < Frames.length; i++)
			this.add(Frames[i]);
	}
	public void add(StackMapFrame Frame)
	{
		Frames.add(Frame);
	}
	public void setFirstOffset(int Offset)
	{
		int FirstOffsetNow = Frames.get(0).getOffset();
		int Difference = FirstOffsetNow - Offset;
		
		if(Difference < 0)
			return;
		
		for(int i = 0; i < Frames.size(); i++)
			Frames.get(i).setOffset(Math.abs(Frames.get(i).getOffset() - Difference));
	}
	public String toString()
	{
		InitFrames();
		
		int FrameSize = Frames.size();
		Information = Util.intToHex((FrameSize), 4);
		
		for(int i = 0; i < FrameSize; i++)
			Information += Frames.get(i).toString();
		
		return super.toString();
	}
	public void SortFrames()
	{
		for(int i = 1; i < Frames.size(); i++)
		{
			for(int j = 0; j < Frames.size(); j++)
			{
				if(Frames.get(i).getOffset() < Frames.get(j).getOffset())
				{
					int temp = Frames.get(i).getOffset();
					Frames.get(i).setOffset(Frames.get(j).getOffset());
					Frames.get(j).setOffset(temp);
					
					temp = Frames.get(i).getLocals();
					Frames.get(i).setLocals(Frames.get(j).getLocals());
					Frames.get(j).setLocals(temp);
				}
				/*
				if(Frames.get(i).getLocals() > Frames.get(j).getLocals())
				{
					int temp = Frames.get(i).getLocals();
					Frames.get(i).setLocals(Frames.get(j).getLocals());
					Frames.get(j).setLocals(temp);
				}*/
			}
		}
	}
	private void InitFrames() 
	{
		SortFrames();
		
		for(int j = 0; j < Frames.size(); j++)
		{
			int Locals = Frames.get(j).getLocals();
			int Offset = Frames.get(j).getOffset();
			int BeforeLocals = 0;
			int BeforeOffset = 0;
			int Type = 0;
			int OffsetDelta = 0;
			
			String Information = "";
			
			if(j > 0)
			{
				Frames.get(j).isFirst(false);
				
				BeforeLocals = Frames.get(j - 1).getLocals();
				BeforeOffset = Frames.get(j - 1).getOffset();
				
				if(BeforeOffset > 0 && Offset != BeforeOffset)
					OffsetDelta = Offset - BeforeOffset - 1;
				else OffsetDelta = Offset;

				if(Locals == BeforeLocals)
					Type = StackMapType.Same + OffsetDelta;
				
				else if(Locals < BeforeLocals)
				{
					int Difference = Math.abs(Locals - BeforeLocals);
					
					Type = StackMapType.ChopMax - Difference;
					LineNumTable.add(new LineNumberFrame(Offset, LineNumTable.get(LineNumTable.size() - 1).getLineNumber() + 1));
				}
				
				else if(BeforeLocals > 0 && Locals > BeforeLocals)
					Type = StackMapType.Append + Locals - BeforeLocals - 1; //First는 둘다 있으므로 상쇄
				
				else Type = StackMapType.Append + Locals - 1 - 1;
				
				if(Type >= StackMapType.Append)
				{
					for(int i = 0; i < Type - 251; i++)
						Information += TypeInfo.getCode(TypeInfo.Integer);
				}
			}
			else
			{
				Frames.get(j).isFirst(true);
				Frames.get(j).hasInputStatement(hasInputStatement);
				
				Type = StackMapType.Append + Locals - 1 - 1; //하나는 First뺀거고, 하나는 사이즈->인덱스이므로 1 더뺌
			
				if(BeforeOffset > 0 && Offset != BeforeOffset)
					OffsetDelta = Offset - BeforeOffset - 1;
				else OffsetDelta = Offset;
				
				if(Type >= StackMapType.Append)
				{
					int loop = 0;
					if(hasInputStatement == true)
						loop = 252;
					else loop = 251;
					
					for(int i = 0; i < Type - loop; i++)
						Information += TypeInfo.getCode(TypeInfo.Integer);
				}
			}
			Frames.get(j).setType(Type);
			Frames.get(j).setOffsetDelta(OffsetDelta);
			Frames.get(j).setInformation(Information);
		}
	}

	public void CopyFrom(StackMapTable Clone)
	{
		this.Frames = Clone.Frames;
	}
	public void Modify(int Offset, StackMapFrame Frame)
	{
		for(int i = 0; i < Frames.size(); i++)
		{
			if(Frames.get(i).getOffset() == Offset)
			{
				Frames.set(i, Frame);
				break;
			}
		}
	}
	public void hasInputStatement(boolean value)
	{
		hasInputStatement = value;
	}
}
