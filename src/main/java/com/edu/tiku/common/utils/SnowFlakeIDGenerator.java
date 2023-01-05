package com.edu.tiku.common.utils;

import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class SnowFlakeIDGenerator {

    private static final String HOST_NAME = "HOSTNAME";

    private static final String DATACENTER_ID_ENV = "DATACENTER";

    /**
     * 起始的时间戳
     */
    private final static long START_STAMP = 1635696000000L;

    /**
     * 序列号占用的位数
     */
    private final static long SEQUENCE_BIT = 12;

    /**
     * 机器标识占用的位置
     */
    private final static long MACHINE_BIT = 5;

    /**
     * 数据中心占用的位置
     */
    private final static long DATACENTER_BIT = 0;

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    /**
     * 数据中心标识
     */
    private final long datacenterId;
    /**
     * 机器标识
     */
    private final long machineId;
    /**
     * 序列号
     */
    private long sequence = 0L;
    /**
     * 上一次时间戳
     */
    private long lastStmp = -1L;

    private static final SnowFlakeIDGenerator DEFAULT_GENERATOR = new SnowFlakeIDGenerator(
            Optional.ofNullable(System.getenv(DATACENTER_ID_ENV)).map(Integer::parseInt)
                    .orElse((int) (System.currentTimeMillis() % (1 << DATACENTER_BIT))),
            Optional.ofNullable(getMachineId()).orElse((int) (System.currentTimeMillis() % (1 << MACHINE_BIT))));

    public SnowFlakeIDGenerator(final long datacenterId, final long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    public static long nextNumber() {
        return DEFAULT_GENERATOR.nextId();
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStamp = getNewStamp();
        if (currStamp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStamp == lastStmp) {
            // 相同毫秒内，序列号自增
            sequence = sequence + 1 & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为0
            sequence = 0L;
        }
        lastStmp = currStamp;
        // 时间戳部分 + 数据中心部分 + 机器标识部分 + 序列号部分
        return currStamp - START_STAMP << TIMESTAMP_LEFT | datacenterId << DATACENTER_LEFT | machineId << MACHINE_LEFT
                | sequence;
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStmp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private static Integer getMachineId() {
        String hostname = System.getenv(HOST_NAME);
        if(StringUtils.isEmpty(hostname)) {
            // env取不到情况下
            hostname = getMachineIdByInetAddress();
            if (StringUtils.isEmpty(hostname)) {
                return null;
            }
        }
        // 匹配hostname最后的数字作为机器的id
        Pattern compile = compile("\\d+$");
        Matcher matcher = compile.matcher(hostname);
        return matcher.find() ? Integer.parseInt(matcher.group()) : null;
    }

    private static String getMachineIdByInetAddress() {
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostName;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }


}
