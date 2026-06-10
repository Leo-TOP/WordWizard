package wordwizard.integration;

import java.util.Random;

/**
 * Deterministic stand-in for the ONNX embedding model.
 * Same text always yields the same vector (so duplicate detection works),
 * different texts yield effectively orthogonal vectors (cosine distance ~1).
 */
public final class TestVectors {
    public static final int DIMENSION = 384;

    private TestVectors() {}

    public static float[] vectorFor(String text) {
        Random random = new Random(text.trim().toLowerCase().hashCode());
        float[] vector = new float[DIMENSION];
        for (int i = 0; i < DIMENSION; i++) {
            vector[i] = random.nextFloat() * 2 - 1;
        }
        return vector;
    }

    /** A vector very close (cosine-wise) to the given one. */
    public static float[] near(float[] base) {
        float[] vector = base.clone();
        for (int i = 0; i < vector.length; i++) {
            vector[i] += 0.01f;
        }
        return vector;
    }
}
