package com.Bytecode.util;


public class Bytecode {

	public final static String Iconst_m1 = "02";
	public final static String Iconst_0 = "03";
	public final static String Iconst_1 = "04";
	public final static String Iconst_2 = "05";
	public final static String Iconst_3 = "06";
	public final static String Iconst_4 = "07";
	public final static String Iconst_5 = "08";
	public final static String Bipush(int value) { return "10" + util.intToHex((value), 2); }
	public final static String Sipush(int value) { return "11" + util.intToHex((value), 4); }
	public final static String Ldc(int index) { return "12" + util.intToHex((index), 2); }
	public final static String Iload(int index) { return "15" + util.intToHex((index), 2); }
	public final static String Iload_1 = "1B";
	public final static String Iload_2 = "1C";
	public final static String Iload_3 = "1D";
	public final static String Aload_0 = "2A";
	public final static String Aload_1 = "2B";
	public final static String Istore(int index) { return "36" + util.intToHex((index), 2); }
	public final static String Istore_1 = "3C";
	public final static String Istore_2 = "3D";
	public final static String Istore_3 = "3E";
	public final static String Astore_1 = "4C";
	public final static String Dup = "59";
	public final static String Imul = "68";
	public final static String Idiv = "6C";
	public final static String Iadd = "60";
	public final static String Isub = "64";
	public final static String iinc(int VariableIndex, int IncreaseAmount) { return "84" + util.intToHex((VariableIndex), 2) + util.intToHex((IncreaseAmount), 2); }
	public final static String Ifeq(int Goto) { return "99" + util.intToHex((Goto), 4); }
	public final static String Ifne(int Goto) { return "9A" + util.intToHex((Goto), 4); }
	public final static String Iflt(int Goto) { return "9B" + util.intToHex((Goto), 4); }
	public final static String Ifge(int Goto) { return "9C" + util.intToHex((Goto), 4); }
	public final static String Ifgt(int Goto) { return "9D" + util.intToHex((Goto), 4); }
	public final static String Ifle(int Goto) { return "9E" + util.intToHex((Goto), 4); }
	public final static String If_icmpeq(int Goto) { return "9F" + util.intToHex((Goto), 4); }
	public final static String If_icmpne(int Goto) { return "A0" + util.intToHex((Goto), 4); }
	public final static String If_icmplt(int Goto) { return "A1" + util.intToHex((Goto), 4); }
	public final static String If_icmpge(int Goto) { return "A2" + util.intToHex((Goto), 4); }
	public final static String If_icmpgt(int Goto) { return "A3" + util.intToHex((Goto), 4); }
	public final static String If_icmple(int Goto) { return "A4" + util.intToHex((Goto), 4); }
	public final static String Goto(int Goto) { return "A7" + util.intToHex((Goto), 4); }
	public final static String Return = "B1";
	public final static String Getstatic(int index) { return "B2" + util.intToHex((index), 4); }
	public final static String Invokevirtual(int index) { return "B6" + util.intToHex((index), 4); }
	public final static String Invokespecial(int index) { return "B7" +  util.intToHex((index), 4); }
	public final static String New(int index) { return "BB" + util.intToHex((index), 4); }
}
