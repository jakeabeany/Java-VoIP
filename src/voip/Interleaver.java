package voip;

/**
 *
 * @author Jake McVey & Matty Williams
 */
public class Interleaver {

    byte[] interleavedPackets;

    /**
     * Public API method
     *
     * @param array
     */
    public void interleave(byte[] array) {
        byte[][] matrix = convert2D(array, array.length);
        byte[][] rotatedMatrix = rotateMatrix(matrix, matrix.length);
        interleavedPackets = convert1D(rotatedMatrix, rotatedMatrix.length);
    }

    /**
     * Public API method
     *
     * @param matrix
     */
    public void deInterleave(byte[] array) {
        byte[][] matrix = convert2D(array, array.length);
        byte[][] deRotatedMatrix = deRotateMatrix(matrix, matrix.length);
        interleavedPackets = convert1D(deRotatedMatrix, deRotatedMatrix.length);
    }

    /**
     * Converts a 2D array into a 1D array.
     *
     * @param matrix
     * @param size
     * @return
     */
    private byte[] convert1D(byte[][] matrix, int size) {
        byte[] array = new byte[size * size];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                array[(i * size) + j] = matrix[i][j];
            }
        }
        return array;
    }
    
    /**
     * Converts a 1D array into a 2D array.
     *
     * @param matrix
     * @param size
     * @return
     */
    private byte[][] convert2D(byte[] array, int size) {
        byte[][] matrix = new byte[size][size];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                matrix[i][j] = array[(i * size) + j];
            }
        }
        return matrix;
    }

    /**
     * Rotates a 2D array of AudioPackets 90deg clockwise.
     *
     * @param matrix
     * @param size
     * @return
     */
    private byte[][] rotateMatrix(byte[][] matrix, int size) {
        byte[][] dest = new byte[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                dest[i][j] = matrix[size - j - 1][i];
            }
        }
        return dest;
    }

    /**
     * 'De-rotates' a 2D array of AudioPackets (rotates 90deg anti-clockwise).
     *
     * @param matrix
     * @param size
     * @return
     */
    private byte[][] deRotateMatrix(byte[][] matrix, int size) {
        System.out.println("De-rotating matrix...");
        byte[][] dest = new byte[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                dest[i][j] = matrix[j][size - i - 1];
            }
        }
        return dest;
    }

}
