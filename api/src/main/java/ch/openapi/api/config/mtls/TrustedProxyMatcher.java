package ch.openapi.api.config.mtls;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class TrustedProxyMatcher {

    private final List<CidrRange> ranges;

    public TrustedProxyMatcher(List<String> cidrs) {
        this.ranges = new ArrayList<>();
        for (String cidr : cidrs) {
            this.ranges.add(CidrRange.parse(cidr));
        }
    }

    public boolean isTrusted(String ip) {
        if (ip == null) return false;
        try {
            InetAddress addr = InetAddress.getByName(ip);
            for (CidrRange range : ranges) {
                if (range.contains(addr)) return true;
            }
        } catch (UnknownHostException ignored) {
        }
        return false;
    }

    private static final class CidrRange {
        private final byte[] network;
        private final int prefixLength;

        private CidrRange(byte[] network, int prefixLength) {
            this.network = network;
            this.prefixLength = prefixLength;
        }

        static CidrRange parse(String cidr) {
            int slash = cidr.indexOf('/');
            String host = slash >= 0 ? cidr.substring(0, slash) : cidr;
            try {
                InetAddress addr = InetAddress.getByName(host);
                int bits = addr.getAddress().length * 8;
                int prefix = slash >= 0 ? Integer.parseInt(cidr.substring(slash + 1)) : bits;
                if (prefix < 0 || prefix > bits) {
                    throw new IllegalArgumentException("Invalid CIDR prefix: " + cidr);
                }
                return new CidrRange(addr.getAddress(), prefix);
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException("Invalid CIDR: " + cidr, e);
            }
        }

        boolean contains(InetAddress addr) {
            byte[] candidate = addr.getAddress();
            if (candidate.length != network.length) return false;
            int fullBytes = prefixLength / 8;
            int remainingBits = prefixLength % 8;
            for (int i = 0; i < fullBytes; i++) {
                if (candidate[i] != network[i]) return false;
            }
            if (remainingBits == 0) return true;
            int mask = 0xFF << (8 - remainingBits) & 0xFF;
            return (candidate[fullBytes] & mask) == (network[fullBytes] & mask);
        }
    }
}
