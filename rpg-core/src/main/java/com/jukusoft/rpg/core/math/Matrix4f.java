package com.jukusoft.rpg.core.math;

import com.jukusoft.rpg.core.utils.BufferUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An 4x4 Matrix
 *
 * a b c d
 * e f g h
 * i j k l
 * m n o p
 *
 * Created by Justin on 21.11.2016.
 */
public class Matrix4f implements Serializable, Cloneable {

    /**
     * every float requires 4 bytes
     */
    private static final int FLOAT_IN_BYTES = 4;

    /**
     * direct (Off Heap) byte buffer to avoid garbage collector cycles
     */
    protected volatile ByteBuffer buffer = null;

    /**
     * direct (Off Heap) float buffer to avoid garbage collector cycles
     *
     * copy of buffer
     */
    protected volatile FloatBuffer floatBuffer = null;

    //flag, if vector is readonly
    protected AtomicBoolean readonly = new AtomicBoolean(false);

    public Matrix4f(final float a, final float b, final float c, final float d,
                    final float e, final float f, final float g, final float h,
                    final float i, final float j, final float k, final float l,
                    final float m, final float n, final float o, final float p) {
        //allocate new direct (Off Heap) byte buffer with size of 2 bytes
        this.buffer = BufferUtils.createByteBuffer(FLOAT_IN_BYTES * (4 * 4));

        //convert to float buffer to access data easely
        this.floatBuffer = this.buffer.asFloatBuffer();

        //set values

        //1. row
        set(0, 0, a);
        set(1, 0, b);
        set(2, 0, c);
        set(3, 0, d);

        //2. row
        set(0, 1, e);
        set(1, 1, f);
        set(2, 1, g);
        set(3, 1, h);

        //3. row
        set(0, 2, i);
        set(1, 2, j);
        set(2, 2, k);
        set(3, 2, l);

        //4. row
        set(0, 3, m);
        set(1, 3, n);
        set(2, 3, o);
        set(3, 3, p);
    }

    public Matrix4f (ByteBuffer buffer) {
        //allocate new direct (Off Heap) byte buffer with size of 2 bytes
        this.buffer = buffer;

        //convert to float buffer to access data easely
        this.floatBuffer = this.buffer.asFloatBuffer();

        //check size
        if (this.floatBuffer.limit() < 16) {
            throw new IllegalArgumentException("FloatBuffer requires size of 16 float values, current limit: " + this.floatBuffer.limit());
        }
    }

    public Matrix4f (Matrix4f matrix) {
        //allocate new direct (Off Heap) byte buffer with size of 2 bytes
        this.buffer = BufferUtils.createByteBuffer(FLOAT_IN_BYTES * (4 * 4));

        //convert to float buffer to access data easely
        this.floatBuffer = this.buffer.asFloatBuffer();

        //copy values
        this.buffer.put(matrix.getBuffer());
    }

    public Matrix4f () {
        //allocate new direct (Off Heap) byte buffer with size of 2 bytes
        this.buffer = BufferUtils.createByteBuffer(FLOAT_IN_BYTES * (4 * 4));

        //convert to float buffer to access data easely
        this.floatBuffer = this.buffer.asFloatBuffer();

        //fill matrix with 0
        for (int i = 0; i < 9; i++) {
            //set value to 0
            this.floatBuffer.put(i, 0);
        }
    }

    /**
    * set value
     *
     * @param column column
     * @param row row
     * @param value value
    */
    public void set (final int column, final int row, float value) {
        if (readonly.get()) {
            throw new IllegalStateException("vector is readonly.");
        }

        //save value
        this.floatBuffer.put(index(column, row), value);
    }

    public void set (Matrix4f matrix) {
        //get original float buffer
        FloatBuffer buffer = matrix.getFloatBuffer();

        //copy all values
        for (int i = 0; i < 16; i++) {
            this.floatBuffer.put(i, buffer.get(i));
        }
    }

    /**
    * set an full row
    */
    public void setRow (final int row, final float c1, final float c2, final float c3, final float c4) {
        set(0, row, c1);
        set(1, row, c1);
        set(2, row, c1);
        set(3, row, c1);
    }

    /**
    * get value of matrix column / row
     *
     * @param column column
     * @param row row
     *
     * @return value
    */
    public float get (final int column, final int row) {
        return this.floatBuffer.get(index(column, row));
    }

    /**
    * add matrix
    */
    public void add (Matrix4f matrix) {
        //iterate through all matrix entries
        for (int c = 0; c <= 3; c++) {
            for (int r = 0; r <= 3; r++) {
                //add entry
                this.set(c, r, (get(c, r) + matrix.get(c, r)));
            }
        }
    }

    /**
     * sub matrix
     */
    public void sub (Matrix4f matrix) {
        //iterate through all matrix entries
        for (int c = 0; c <= 3; c++) {
            for (int r = 0; r <= 3; r++) {
                //add entry
                this.set(c, r, (get(c, r) - matrix.get(c, r)));
            }
        }
    }

    /**
    * multiply with matrix
     *
     * @param matrix matrix to multiply with
    */
    public void mul (Matrix4f matrix) {
        //create an copy of current matrix
        Matrix4f currentMatrix = this.copy();

        //iterate through all matrix entries
        for (int c = 0; c <= 3; c++) {
            for (int r = 0; r <= 3; r++) {
                float value = 0;

                //System.out.println("multiplize for (" + c + ", " + r + "):");

                //multiply
                for (int i = 3; i >= 0; i--) {
                    value += currentMatrix.get(3 - i, r) * matrix.get(c, 3 - i);
                    //System.out.println("value += (" + (3 - i) + ", " + r + ") * (" + c + ", " + (3 - i) + ") --> " + currentMatrix.get(3 - i, r) + " * " + matrix.get(c, 3 - i) + " = " + (currentMatrix.get(3 - i, r) * matrix.get(c, 3 - i)));
                }

                //System.out.println("value: " + value + "\n\n");

                //set new value
                this.set(c, r, value);
            }
        }

        //clean up memory from copy
        currentMatrix.release();
    }

    /**
    * scale matrix
    */
    public void scale (float factor) {
        //iterate through all matrix entries
        /*for (int c = 0; c <= 3; c++) {
            for (int r = 0; r <= 3; r++) {
                //scale value
                this.set(c, r, this.get(c, r) * factor);
            }
        }*/

        this.scale(factor, factor, factor, this);
    }

    /**
    * scale matrix
    */
    public void scale (final float x, final float y, final float z, Matrix4f dest) {
        //calculate and set new values
        dest.set(0, 0, get(0, 0) * x);
        dest.set(0, 1, get(0, 1) * x);
        dest.set(0, 2, get(0, 2) * x);
        dest.set(0, 3, get(0, 3) * x);
        dest.set(1, 0, get(1, 0) * y);
        dest.set(1, 1, get(1, 1) * y);
        dest.set(1, 2, get(1, 2) * y);
        dest.set(1, 3, get(1, 3) * y);
        dest.set(2, 0, get(2, 0) * z);
        dest.set(2, 1, get(2, 1) * z);
        dest.set(2, 2, get(2, 2) * z);
        dest.set(2, 3, get(2, 3) * z);

        //copy values
        dest.set(3, 0, get(3, 0));
        dest.set(3, 1, get(3, 1));
        dest.set(3, 2, get(3, 2));
        dest.set(3, 3, get(3, 3));
    }

    /**
    * transform matrix
     *
     * dreht die Matrix um 90 Grad
    */
    public void transform () {
        //copy current matrix
        Matrix4f oldMatrix = this.copy();

        //iterate through all matrix entries
        for (int c = 0; c <= 3; c++) {
            for (int r = 0; r <= 3; r++) {
                //scale value
                this.set(c, r, oldMatrix.get(r, c));
            }
        }
    }

    /**
    * calculate index address
     *
     * @param column column
     * @param row row
     *
     * @return index position in array
    */
    protected static int index (final int column, final int row) {
        if (column > 3 || row > 3) {
            throw new ArrayIndexOutOfBoundsException("entry column: " + column + ", row: " + row + " doesnt exists in 4x4 matrix");
        }

        return column * 4 + row;
    }

    /**
    * calculate determinate of 4x4 matrix with la blace
     *
     * @return determinate
    */
    public float det () {
        /**
        *  a b c d
         * e f g h
         * i j k l
         * m n o p
        */

        //TODO: optimize this code, dont copy values in new variables

        //1. row
        final float a = get(0, 0);
        final float b = get(1, 0);
        final float c = get(2, 0);
        final float d = get(3, 0);

        //2. row
        final float e = get(0, 1);
        final float f = get(1, 1);
        final float g = get(2, 1);
        final float h = get(3, 1);

        //3. row
        final float i = get(0, 2);
        final float j = get(1, 2);
        final float k = get(2, 2);
        final float l = get(3, 2);

        //4. row
        final float m = get(0, 3);
        final float n = get(1, 3);
        final float o = get(2, 3);
        final float p = get(3, 3);

        //a⋅e⋅i+b⋅f⋅g+c⋅d⋅h−g⋅e⋅c−h⋅f⋅a−i⋅d⋅b

        //http://matheguru.com/lineare-algebra/207-determinante.html

        final float v1 = a * DetUtils.calculateDet3x3(
                f, g, h,
                j, k, l,
                n, o, p
        );

        final float v2 = b * DetUtils.calculateDet3x3(
                e, g, h,
                i, k, l,
                m, o, p
        );

        final float v3 = c * DetUtils.calculateDet3x3(
                e, f, h,
                i, j, l,
                m, n, p
        );

        final float v4 = d * DetUtils.calculateDet3x3(
                e, f, g,
                i, j, k,
                m, n, o
        );

        return v1 - v2 + v3 - v4;
    }

    /**
    * reset all entries to 0 values
    */
    public void reset () {
        //iterate through all values
        for (int i = 0; i < 16; i++) {
            this.floatBuffer.put(i, 0);
        }
    }

    /**
    * set all values to identity matrix (Einheitsmatrix)
    */
    public void identity () {
        this.reset();

        for (int i = 0; i < 4; i++) {
            this.set(i, i, 1);
        }
    }

    /**
     * alias to identity()
     */
    public void setIdentityMatrix () {
        this.identity();
    }

    /**
    * translate matrix
    */
    public void translate (final float x, final float y, final float z) {
        this.set(3, 0, get(0, 0) * x + get(1, 0) * y + get(2, 0) * z + get(3, 0));
        this.set(3, 1, get(0, 1) * x + get(1, 1) * y + get(2, 1) * z + get(3, 1));
        this.set(3, 2, get(0, 2) * x + get(1, 2) * y + get(2, 2) * z + get(3, 2));
        this.set(3, 3, get(0, 3) * x + get(1, 3) * y + get(2, 3) * z + get(3, 3));
    }

    /**
     * translate matrix
     */
    public Matrix4f translate (Vector3f vector) {
        this.translate(vector.getX(), vector.getY(), vector.getZ());

        return this;
    }

    public void rotateX (float angel) {
        this.rotateX(angel, this);
    }

    public void rotateX (float angel, Matrix4f dest) {
        float sin = (float) Math.sin((double) angel);
        float cos = (float) Math.cos((double) angel);

        float rm21 = -sin;
        float nm10 = this.get(1, 0) * cos + this.get(2, 0) * sin;
        float nm11 = this.get(1, 1) * cos + this.get(2, 1) * sin;
        float nm12 = this.get(1, 2) * cos + this.get(2, 2) * sin;
        float nm13 = this.get(1, 3) * cos + this.get(2, 3) * sin;

        dest.set(2, 0, this.get(1, 0) * rm21 + this.get(2, 0) * cos);
        dest.set(2, 1, this.get(1, 1) * rm21 + this.get(2, 1) * cos);
        dest.set(2, 2, this.get(1, 2) * rm21 + this.get(2, 2) * cos);
        dest.set(2, 3, this.get(1, 3) * rm21 + this.get(2, 3) * cos);

        //copy values to destination matrix
        dest.set(1, 0, nm10);
        dest.set(1, 1, nm11);
        dest.set(1, 2, nm12);
        dest.set(1, 3, nm13);
        dest.set(0, 0, get(0, 0));
        dest.set(0, 1, get(0, 01));
        dest.set(0, 2, get(0, 02));
        dest.set(0, 3, get(0, 03));
        dest.set(3, 0, get(3, 0));
        dest.set(3, 1, get(3, 1));
        dest.set(3, 2, get(3, 2));
        dest.set(3, 3, get(3, 3));
    }

    public void rotateY (float angel) {
        this.rotateY(angel, this);
    }

    public void rotateY (float angel, Matrix4f dest) {
        //calculate sinus and cosinus
        float sin = (float) Math.sin((double) angel);
        float cos = (float) Math.cos((double) angel);

        float rm02 = -sin;
        float nm00 = get(0, 0) * cos + get(2, 0) * rm02;
        float nm01 = get(0, 1) * cos + get(2, 1) * rm02;
        float nm02 = get(0, 2) * cos + get(2, 2) * rm02;
        float nm03 = get(0, 3) * cos + get(2, 3) * rm02;

        dest.set(2, 0, get(0, 0) * sin + get(2, 0) * cos);
        dest.set(2, 1, get(0, 1) * sin + get(2, 1) * cos);
        dest.set(2, 2, get(0, 2) * sin + get(2, 2) * cos);
        dest.set(2, 3, get(0, 3) * sin + get(2, 3) * cos);

        dest.set(0, 0, nm00);
        dest.set(0, 1, nm01);
        dest.set(0, 2, nm02);
        dest.set(0, 3, nm03);

        dest.set(1, 0, get(1, 0));
        dest.set(1, 1, get(1, 1));
        dest.set(1, 2, get(1, 2));
        dest.set(1, 3, get(1, 3));
        dest.set(3, 0, get(3, 0));
        dest.set(3, 1, get(3, 1));
        dest.set(3, 2, get(3, 2));
        dest.set(3, 3, get(3, 3));
    }

    public void rotateZ (float angel) {
        this.rotateZ(angel, this);
    }

    public void rotateZ (float angel, Matrix4f dest) {
        //calculate sinus and cosinus
        float sin = (float) Math.sin((double) angel);
        float cos = (float) Math.cos((double) angel);

        float rm10 = -sin;
        float nm00 = get(0, 0) * cos + get(1, 0) * sin;
        float nm01 = get(0, 1) * cos + get(1, 1) * sin;
        float nm02 = get(0, 2) * cos + get(1, 2) * sin;
        float nm03 = get(0, 3) * cos + get(1, 3) * sin;

        dest.set(1, 0, get(0, 0) * rm10 + get(1, 0) * cos);
        dest.set(1, 1, get(0, 1) * rm10 + get(1, 1) * cos);
        dest.set(1, 2, get(0, 2) * rm10 + get(1, 2) * cos);
        dest.set(1, 3, get(0, 3) * rm10 + get(1, 3) * cos);
        dest.set(0, 0, nm00);
        dest.set(0, 1, nm01);
        dest.set(0, 2, nm02);
        dest.set(0, 3, nm03);
        dest.set(2, 0, get(2, 0));
        dest.set(2, 1, get(2, 1));
        dest.set(2, 2, get(2, 2));
        dest.set(2, 3, get(2, 3));
        dest.set(3, 0, get(3, 0));
        dest.set(3, 1, get(3, 1));
        dest.set(3, 2, get(3, 2));
        dest.set(3, 3, get(3, 3));
    }

    public void print (boolean lineBreak) {
        System.out.println(this.toString(lineBreak));
    }

    public String toString (boolean lineBreak) {
        String ln = "\n";
        String str = "";

        if (!lineBreak) {
            ln = "";
        }

        str += "(" + get(0, 0) + ", " + get(1, 0) + ", " + get(2, 0) + ", " + get(3, 0) + "), " + ln;
        str += "(" + get(0, 1) + ", " + get(1, 1) + ", " + get(2, 1) + ", " + get(3, 1) + "), " + ln;
        str += "(" + get(0, 2) + ", " + get(1, 2) + ", " + get(2, 2) + ", " + get(3, 2) + "), " + ln;
        str += "(" + get(0, 3) + ", " + get(1, 3) + ", " + get(2, 3) + ", " + get(3, 3) + ")" + ln;

        return str;
    }

    @Override
    public String toString () {
        return this.toString(false);
    }

    /**
    * generate orthogonal matrix
    */
    public void setOrtho2D (final float left, final float right, final float bottom, final float top) {
        //set identity matrix first
        this.identity();

        //set values
        this.set(0, 0, (2.0f / (right - left)));
        this.set(1, 1, (2.0f / (top - bottom)));
        this.set(2, 2, -1f);
        this.set(3, 0, -(right + left) / (right - left));
        this.set(3, 1, -(top + bottom) / (top - bottom));
    }

    public Matrix4f copy () {
        //create new matrix
        Matrix4f matrix = new Matrix4f();

        //iterate through all matrix entries
        for (int c = 0; c <= 3; c++) {
            for (int r = 0; r <= 3; r++) {
                //copy value
                matrix.set(c, r, this.get(c, r));
            }
        }

        return matrix;
    }

    @Override
    public Matrix4f clone () {
        return this.copy();
    }

    public boolean equals (Matrix4f matrix) {
        //check every value

        //iterate through all matrix entries
        for (int c = 0; c <= 3; c++) {
            for (int r = 0; r <= 3; r++) {
                //check value
                if (this.get(c, r) != matrix.get(c, r)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof Matrix4f)) {
            throw new UnsupportedOperationException("You can only check, if matrizes are equals. obj class: " + obj.getClass().getName());
        }

        return this.equals((Matrix4f) obj);
    }

    public FloatBuffer getFloatBuffer () {
        return this.floatBuffer;
    }

    public ByteBuffer getBuffer () {
        return this.buffer;
    }

    /**
     * cleanUp and release memory
     */
    @SuppressWarnings("all")
    public void release () {
        Field cleanerField = null;
        try {
            cleanerField = floatBuffer.getClass().getDeclaredField("cleaner");
            cleanerField.setAccessible(true);
            sun.misc.Cleaner cleaner = (sun.misc.Cleaner) cleanerField.get(floatBuffer);
            cleaner.clean();
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        }

        this.floatBuffer = null;
    }

}
