package dynastxu.noitacore.utils;

import org.jspecify.annotations.NonNull;

public class SmoothVectorField {
    protected final Noise3D noiseA;
    protected final Noise3D noiseB;
    protected final Noise3D noiseC;

    public SmoothVectorField(long seedA, long seedB, long seedC) {
        this.noiseA = new Noise3D(seedA);
        this.noiseB = new Noise3D(seedB);
        this.noiseC = new Noise3D(seedC);
    }

    /**
     * 输入坐标和最大模长 n，输出矢量。
     *
     * @param scale 噪声空间特征尺度，越大方向变化越缓慢。
     */
    public double @NonNull [] vectorAt(double x, double y, double z,
                                              double n, double scale) {
        double maxLen = Math.abs(n);
        double invScale = 1.0 / Math.max(scale, 1e-6);

        // 缩放坐标，控制噪声梯度
        double sx = x * invScale;
        double sy = y * invScale;
        double sz = z * invScale;

        double v1 = noiseA.noise(sx, sy, sz);
        double v2 = noiseB.noise(sx, sy, sz);
        double v3 = noiseC.noise(sx, sy, sz);

        double theta = (v1 + 1.0) * 0.5 * Math.PI;
        double phi = (v2 + 1.0) * Math.PI;
        double r = maxLen * (0.55 + 0.45 * v3);

        double sinTheta = Math.sin(theta);
        double a = r * sinTheta * Math.cos(phi);
        double b = r * sinTheta * Math.sin(phi);
        double c = r * Math.cos(theta);

        return new double[]{a, b, c};
    }

    /**
     * 基于 64 位哈希的三维 Value Noise。
     * 不使用传统 Perlin 的 256 置换表，避免短周期重复。
     */
    protected static class Noise3D {
        protected final long seed;

        Noise3D(long seed) {
            this.seed = seed;
        }

        protected static long hash(long x, long y, long z, long seed) {
            long h = seed;

            h = h * 0x9E3779B97F4A7C15L + x;
            h = (h ^ (h >>> 30)) * 0xBF58476D1CE4E5B9L;
            h = (h ^ (h >>> 27)) * 0x94D049BB133111EBL;
            h ^= h >>> 31;

            h = h * 0x9E3779B97F4A7C15L + y;
            h = (h ^ (h >>> 29)) * 0xC2B2AE3D27D4EB4FL;
            h = (h ^ (h >>> 31)) * 0x165667B19E3779F9L;
            h ^= h >>> 32;

            h = h * 0x9E3779B97F4A7C15L + z;
            h = (h ^ (h >>> 30)) * 0xBF58476D1CE4E5B9L;
            h = (h ^ (h >>> 27)) * 0x94D049BB133111EBL;
            h ^= h >>> 31;

            return h;
        }

        protected static double fade(double t) {
            return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
        }

        protected static double lerp(double a, double b, double t) {
            return a + (b - a) * t;
        }

        /**
         * 返回 [-1, 1] 的连续平滑噪声值
         */
        protected double noise(double x, double y, double z) {
            double fx = Math.floor(x);
            double fy = Math.floor(y);
            double fz = Math.floor(z);

            long x0 = (long) fx;
            long y0 = (long) fy;
            long z0 = (long) fz;
            long x1 = x0 + 1;
            long y1 = y0 + 1;
            long z1 = z0 + 1;

            double tx = x - fx;
            double ty = y - fy;
            double tz = z - fz;

            double u = fade(tx);
            double v = fade(ty);
            double w = fade(tz);

            double c000 = cornerValue(x0, y0, z0);
            double c100 = cornerValue(x1, y0, z0);
            double c010 = cornerValue(x0, y1, z0);
            double c110 = cornerValue(x1, y1, z0);
            double c001 = cornerValue(x0, y0, z1);
            double c101 = cornerValue(x1, y0, z1);
            double c011 = cornerValue(x0, y1, z1);
            double c111 = cornerValue(x1, y1, z1);

            double x00 = lerp(c000, c100, u);
            double x10 = lerp(c010, c110, u);
            double x01 = lerp(c001, c101, u);
            double x11 = lerp(c011, c111, u);

            double y0v = lerp(x00, x10, v);
            double y1v = lerp(x01, x11, v);

            return lerp(y0v, y1v, w);
        }

        protected double cornerValue(long x, long y, long z) {
            long h = hash(x, y, z, seed);
            double unit = (h & 0x7FFFFFFFFFFFFFFFL) / (double) Long.MAX_VALUE;
            return unit * 2.0 - 1.0;
        }
    }
}
