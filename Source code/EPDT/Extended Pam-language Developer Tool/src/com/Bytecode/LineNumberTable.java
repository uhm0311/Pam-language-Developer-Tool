package com.Bytecode;

import java.util.ArrayList;

import com.Bytecode.util.GeneralAttribute;
import com.Bytecode.util.LineNumberFrame;
import com.Bytecode.util.util;


public class LineNumberTable extends GeneralAttribute {

	ArrayList<LineNumberFrame> Frames = new ArrayList<LineNumberFrame>();
	
	public LineNumberTable() { }
	public LineNumberFrame get(int index)
	{
		return Frames.get(index);
	}
	public void set(int index, LineNumberFrame Frame)
	{
		Frames.set(index, Frame);
	}
	public int size()
	{
		return Frames.size();
	}
	public void add(LineNumberFrame[] Frames)
	{
		for(int i = 0; i < Frames.length; i++)
			this.add(Frames[i]);
	}
	public void add(LineNumberFrame Frame)
	{
		boolean isOverlapped = false;
		int OverlappedIndex = -1;
		for(int i = 0; i < Frames.size(); i++)
		{
			if(Frames.get(i).getStartPc() == Frame.getStartPc())
			{
				isOverlapped = true;
				OverlappedIndex = i;
			}
		}
		if(isOverlapped == true)
			Frames.get(OverlappedIndex).setLineNumber(Frame.getLineNumber());
		else Frames.add(Frame);
	}
	public String toString()
	{
		int FrameSize = Frames.size();
		Information = util.intToHex((FrameSize), 4);
		
		ArrayList<Integer> StartPcArrayList = new ArrayList<Integer>();
		ArrayList<Integer> LineNumberArrayList = new ArrayList<Integer>();
		
		for(int i = 0; i < FrameSize; i++)
		{
			StartPcArrayList.add(Frames.get(i).getStartPc());
			LineNumberArrayList.add(Frames.get(i).getLineNumber());
		}
		
		Object[] StartPcArray = util.Sort(StartPcArrayList.toArray(), true);
		Object[] LineNumberArray = util.Sort(LineNumberArrayList.toArray(), true);
		
		ArrayList<LineNumberFrame> SortedFrames = new ArrayList<LineNumberFrame>();
		
		for(int i = 0; i < FrameSize; i++)
			SortedFrames.add(new LineNumberFrame((int)StartPcArray[i], (int)LineNumberArray[i]));
		
		for(int i = 0; i < FrameSize; i++)
			Information += SortedFrames.get(i).toString();
		
		return super.toString();
	}
	public void CopyFrom(LineNumberTable Clone)
	{
		this.Frames = Clone.Frames;
	}
	public boolean containsStartPc(int StartPc) 
	{
		for(int i = 0; i < Frames.size(); i++)
		{
			if(Frames.get(i).getStartPc() == StartPc)
				return true;
		}
		return false;
	}
}
