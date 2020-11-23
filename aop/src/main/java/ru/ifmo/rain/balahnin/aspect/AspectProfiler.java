package ru.ifmo.rain.balahnin.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import ru.ifmo.rain.balahnin.test.Main;

import java.util.*;

@Aspect
public class AspectProfiler {
    private static class MethodNode implements Comparable<MethodNode> {
        ProfilingCharacteristics profilingCharacteristics = new ProfilingCharacteristics();
        Map<String, MethodNode> children = new HashMap<>();
        String name;

        public MethodNode(String name) {
            this.name = name;
        }

        @Override
        public int compareTo(MethodNode o) {
            return Comparator.<MethodNode>
                    comparingLong(methodNode -> methodNode.profilingCharacteristics.getTime())
                    .thenComparingDouble(methodNode -> methodNode.profilingCharacteristics.averageTime())
                    .compare(this, o);
        }
    }

    private final Deque<MethodNode> executionStack = new LinkedList<>();

    @Around("execution(* *(..)) && !within(ru.ifmo.rain.balahnin.aspect.*)")
    public Object allMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        if (!joinPoint.getSignature().getName().equals("main")
                && (Main.packageToProfile == null ||
                !joinPoint.getSignature().getDeclaringType().getPackage().getName()
                        .startsWith(Main.packageToProfile))) {
            return joinPoint.proceed(joinPoint.getArgs());
        }
        MethodNode curMethod = processMethod(joinPoint);
        Object res = null;
        Throwable exception = null;
        long startMs = System.currentTimeMillis();
        try {
            res = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            exception = e;
        }
        long finishMs = System.currentTimeMillis();

        curMethod.profilingCharacteristics.update(finishMs - startMs);
        executionStack.removeLast();

        if (executionStack.isEmpty()) {
            printExecutionTree(0, curMethod);
        }
        if (exception != null) {
            throw exception;
        }
        return res;
    }

    private MethodNode processMethod(ProceedingJoinPoint joinPoint) {
        MethodNode parent = executionStack.peekLast();
        String curName = joinPoint.getSignature().getDeclaringType().getName() + "." + joinPoint.getSignature().getName();

        MethodNode cur;
        if (parent != null) {
            cur = parent.children.getOrDefault(curName, new MethodNode(curName));
            parent.children.putIfAbsent(curName, cur);
        } else {
            cur = new MethodNode(curName);
        }
        executionStack.addLast(cur);
        return cur;
    }

//    @Around("execution(* *.main(..))")
//    public void afterMain(ProceedingJoinPoint joinPoint) {
//        processMethod(joinPoint);
//
//        System.out.println("hello");
//    }

    private void printExecutionTree(long depth, MethodNode node) {
        System.out.println(depthShift(depth) + node.name + ": full time: " + node.profilingCharacteristics.getTime() +
                ", call count: " + node.profilingCharacteristics.getCount() + ", time per call: " + node.profilingCharacteristics.averageTime());
        node.children.values().stream().sorted()
                .forEach(it -> printExecutionTree(depth + 1, it));
    }


    private String depthShift(long depth) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            stringBuilder.append("     ");
        }
        return stringBuilder.toString();
    }
}
