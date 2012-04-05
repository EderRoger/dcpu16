package br.com.leandromoreira.jdcpu16br;

public class Word {

    private final int code;
    private final int a;
    private final int b;
    private final int instruction;

    public Word(final int rawInstruction) {
        instruction = rawInstruction;
        code = rawInstruction & 0xF;
        a = (rawInstruction >> 0x4) & 0x3F;
        b = (rawInstruction >> 0xA);
    }

    public int code() {
        return code;
    }

    public int a() {
        return a;
    }

    public int b() {
        return b;
    }

    public int instruction() {
        return instruction;
    }
}
