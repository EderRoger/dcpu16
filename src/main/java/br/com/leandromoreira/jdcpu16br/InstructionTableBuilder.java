package br.com.leandromoreira.jdcpu16br;

import static br.com.leandromoreira.jdcpu16br.OpCodes.*;

public class InstructionTableBuilder {

    private static final int NUMBER_OF_INSTRUCTIONS = 0x10;
    private static final int ZERO = 0;
    private static final int ONE = 1;

    public Instruction[] instructionSet(final CPU cpu) {
        final Instruction[] instruction = new Instruction[NUMBER_OF_INSTRUCTIONS];
        final ParameterDecoder[] decoder = new ParameterDecoderBuilder(cpu).all();
        
        instruction[NOT_BASIC] = new DefaultInstruction() {

            private int cycles = 2;

            @Override
            public void execute(final Word parameter) {
                final int syscall = (parameter.instruction() >> 0x4) & 0x6;
                final int a = (parameter.instruction() >> (0x4 + 0x6));
                switch (syscall) {
                    case SYSCALL_JSR:
                        cpu.setStackPointer(cpu.getProgramCounter() + ONE);
                        cpu.setProgramCounter(cpu.register(a));
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

            @Override
            public void execute(final Word parameter) {
                cpu.parameterA().write(cpu.parameterB().read());
            }
        };/*
        instruction[ADD] = new DefaultInstruction() {

            @Override
            public void execute(final Word parameter) {
                cpu.register(parameter.a()) += cpu.register(parameter.b());
                overflow = ((cpu.register(parameter.a()] & MASK_16BIT) > ZERO) ? ONE : ZERO;
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[SUB] = new DefaultInstruction() {

            @Override
            public void execute(final Word parameter) {
                cpu.register(parameter.a()] -= cpu.register(parameter.b()];
                overflow = (cpu.register(parameter.a()] < ZERO) ? OxFFFF : ZERO;
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[MUL] = new DefaultInstruction() {

            @Override
            public void execute(final Word parameter) {
                cpu.register(parameter.a()] *= cpu.register(parameter.b()];
                overflow = ((cpu.register(parameter.a()] * cpu.register(parameter.b()]) >> WORD_SIZE) & OxFFFF;
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[DIV] = new DefaultInstruction() {

            @Override
            public void execute(final Word parameter) {
                if (cpu.register(parameter.b()] != ZERO) {
                    cpu.register(parameter.a()] /= cpu.register(parameter.b()];
                    overflow = ((cpu.register(parameter.a()] << WORD_SIZE) / cpu.register(parameter.b()]) & OxFFFF;
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

            @Override
            public void execute(final Word parameter) {
                cpu.register(parameter.a()] = (cpu.register(parameter.b()] == ZERO) ? ZERO : cpu.register(parameter.a()] % cpu.register(parameter.b()];
            }

            @Override
            public int cycles() {
                return 3 + cost;
            }
        };
        instruction[SHL] = new DefaultInstruction() {

            @Override
            public void execute(final Word parameter) {
                cpu.register(parameter.a()] <<= cpu.register(parameter.b()];
                overflow = ((cpu.register(parameter.a()] << cpu.register(parameter.b()]) >> WORD_SIZE) & OxFFFF;
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[SHR] = new DefaultInstruction() {

            @Override
            public void execute(final Word parameter) {
                cpu.register(parameter.a()] >>= cpu.register(parameter.b()];
                overflow = ((cpu.register(parameter.a()] << WORD_SIZE) >> cpu.register(parameter.b()]) & OxFFFF;
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };
        instruction[AND] = new DefaultInstruction() {

            @Override
            public void execute(final Word parameter) {
                cpu.register(parameter.a()] &= cpu.register(parameter.b()];
            }
        };
        instruction[BOR] = new DefaultInstruction() {

            @Override
            public void execute(final Word parameter) {
                cpu.register(parameter.a()] |= cpu.register(parameter.b()];
            }
        };
        instruction[XOR] = new DefaultInstruction() {

            @Override
            public void execute(final Word parameter) {
                cpu.register(parameter.a()] ^= cpu.register(parameter.b()];
            }
        };
        instruction[IFE] = new DefaultInstruction() {

            @Override
            public void execute(final Word parameter) {
                if (cpu.register(parameter.a()] != cpu.register(parameter.b()]) {
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

            @Override
            public void execute(final Word parameter) {
                if (cpu.register(parameter.a()] == cpu.register(parameter.b()]) {
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

            @Override
            public void execute(final Word parameter) {
                if (cpu.register(parameter.a()] < cpu.register(parameter.b()]) {
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

            @Override
            public void execute(final Word parameter) {
                if (cpu.register(parameter.a()] > cpu.register(parameter.b()]) {
                    cost++;
                    defaultSumToNextInstruction = 0;
                }
            }

            @Override
            public int cycles() {
                return 2 + cost;
            }
        };*/

        return instruction;
    }
}
