package br.com.leandromoreira.jdcpu16br;

import static br.com.leandromoreira.jdcpu16br.OpCode.*;

public class CPU {

    private static final int NUMBER_OF_INSTRUCTIONS = 0x10;
    private static final int MEMORY_SIZE = 0x10000;
    private static final int MASK_16BIT = 0xF;
    private static final int OxFFFF = 0xFFFF;
    private static final int WORD_SIZE = 16;
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int A = 0x0;
    private static final int B = 0x1;
    private static final int C = 0x2;
    private static final int X = 0x3;
    private static final int Y = 0x4;
    private static final int Z = 0x5;
    private static final int I = 0x6;
    private static final int J = 0x7;
    private int[] memory = new int[MEMORY_SIZE];
    private int[] register = new int[0x8];
    private int programCounter, stackPointer;
    private int overflow;
    private Instruction[] instruction = new Instruction[NUMBER_OF_INSTRUCTIONS];

    public void fillInstructionTable() {
        instruction[NOT_BASIC] = new DefaultInstruction() {

            private int cycles = 2;

            public void execute(final OpCode parameter) {
                final int syscall = (parameter.instruction() >> 0x4) & 0x6;
                final int a = (parameter.instruction() >> (0x4 + 0x6));
                switch (syscall) {
                    case SYSCALL_JSR:
                        stackPointer = programCounter + ONE;
                        programCounter = register[a];
                        defaultSumToNextInstruction = ZERO;
                        break;
                }
            }

            @Override
            public int cycles() {
                return cycles + cost;
            }
        };
        instruction[SET] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                register[parameter.a()] = register[parameter.b()];
            }
        };
        instruction[ADD] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                register[parameter.a()] += register[parameter.b()];
                overflow = ((register[parameter.a()] & MASK_16BIT) > ZERO) ? ONE : ZERO;
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[SUB] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                register[parameter.a()] -= register[parameter.b()];
                overflow = (register[parameter.a()] < ZERO) ? OxFFFF : ZERO;
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[MUL] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                register[parameter.a()] *= register[parameter.b()];
                overflow = ((register[parameter.a()] * register[parameter.b()]) >> WORD_SIZE) & OxFFFF;
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[DIV] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                if (register[parameter.b()] != ZERO) {
                    register[parameter.a()] /= register[parameter.b()];
                    overflow = ((register[parameter.a()] << WORD_SIZE) / register[parameter.b()]) & OxFFFF;
                } else {
                    overflow = ZERO;
                }
            }

            @Override
            public int cycles() {
                return 3 + cost;
            }
        };
        instruction[MOD] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                register[parameter.a()] = (register[parameter.b()] == ZERO) ? ZERO : register[parameter.a()] % register[parameter.b()];
            }

            @Override
            public int cycles() {
                return 3 + cost;
            }
        };
        instruction[SHL] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                register[parameter.a()] <<= register[parameter.b()];
                overflow = ((register[parameter.a()] << register[parameter.b()]) >> WORD_SIZE) & OxFFFF;
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[SHR] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                register[parameter.a()] >>= register[parameter.b()];
                overflow = ((register[parameter.a()] << WORD_SIZE) >> register[parameter.b()]) & OxFFFF;
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[AND] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                register[parameter.a()] &= register[parameter.b()];
            }
        };
        instruction[BOR] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                register[parameter.a()] |= register[parameter.b()];
            }
        };
        instruction[XOR] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                register[parameter.a()] ^= register[parameter.b()];
            }
        };
        instruction[IFE] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                if (register[parameter.a()] != register[parameter.b()]) {
                    cost++;
                    defaultSumToNextInstruction = 0;
                }
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[IFN] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                if (register[parameter.a()] == register[parameter.b()]) {
                    cost++;
                    defaultSumToNextInstruction = 0;
                }
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[IFG] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                if (register[parameter.a()] < register[parameter.b()]) {
                    cost++;
                    defaultSumToNextInstruction = 0;
                }
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[IFB] = new DefaultInstruction() {

            public void execute(final OpCode parameter) {
                if (register[parameter.a()] > register[parameter.b()]) {
                    cost++;
                    defaultSumToNextInstruction = 0;
                }
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
    }

    public void step() {
        final OpCode op = new OpCode(readFrom(programCounter));
        final Instruction currentInstruction = instruction[op.code()];
        currentInstruction.execute(op);
        programCounter += currentInstruction.sumToPC();
    }

    public int readFrom(final int address) {
        return memory[address];
    }
}