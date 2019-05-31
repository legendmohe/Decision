package com.legendmohe;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 适用场景：各个模块拦截处理touch事件等
 *
 * Created by hexinyu on 2019/5/31.
 */
public class Decision {

    private static class LazyHolder {
        private static final Decision INSTANCE = new Decision();
    }

    private static Decision getInstance() {
        return LazyHolder.INSTANCE;
    }

    private Map<String, Maker> mHolderMap = new HashMap<String, Maker>();

    public synchronized static Maker forEvent(String eventName) {
        if (!getInstance().mHolderMap.containsKey(eventName)) {
            getInstance().mHolderMap.put(eventName, new Maker());
        }
        return getInstance().mHolderMap.get(eventName);
    }

    public synchronized static void remove(String makerName) {
        getInstance().mHolderMap.remove(makerName);
    }

    public synchronized static void clear() {
        getInstance().mHolderMap.clear();
    }

    ///////////////////////////////////private///////////////////////////////////

    public static class Maker {
        List<MakeDecision> mDecisions = new CopyOnWriteArrayList<MakeDecision>();

        public Maker add(MakeDecision decision) {
            mDecisions.add(decision);
            return this;
        }

        public Maker remove(MakeDecision decision) {
            mDecisions.remove(decision);
            return this;
        }

        public <E, R> R getResult(E event) {
            Collections.sort(mDecisions, new Comparator<MakeDecision>() {
                @Override
                public int compare(MakeDecision o1, MakeDecision o2) {
                    return o2.getPriority() - o1.getPriority();
                }
            });
            R result = null;
            for (MakeDecision<E, R> decision : mDecisions) {
                result = decision.onEvent(event);
                if (result != null) {
                    break;
                }
            }
            return result;
        }

        public Maker clear() {
            mDecisions.clear();
            return this;
        }
    }

    public interface MakeDecision<E, R> {

        // return null to invoke next processor
        R onEvent(E event);

        // higher priority gets first
        int getPriority();
    }
}
