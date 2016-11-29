package com.jukusoft.rpg.core.math;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Justin on 30.11.2016.
 */
public class Vector3f {

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

    /**
     * default constructor
     *
     * @param x x coordinate of vector
     * @param y y coordinate of vector
     */
    public Vector3f(final float x, final float y, final float z) {
        //allocate new direct (Off Heap) byte buffer with size of 3 bytes
        this.buffer = ByteBuffer.allocateDirect(FLOAT_IN_BYTES * 3);

        //convert to float buffer to access data easely
        this.floatBuffer = this.buffer.asFloatBuffer();

        //set initial coordinates
        this.floatBuffer.put(0, x);
        this.floatBuffer.put(1, y);
        this.floatBuffer.put(2, z);
    }

    /**
     * default constructor
     */
    public Vector3f() {
        //allocate new direct (Off Heap) byte buffer with size of 3 bytes
        this.buffer = ByteBuffer.allocateDirect(FLOAT_IN_BYTES * 3);

        //convert to float buffer to access data easely
        this.floatBuffer = this.buffer.asFloatBuffer();

        //set initial coordinates
        this.floatBuffer.put(0, 0);
        this.floatBuffer.put(1, 0);
        this.floatBuffer.put(2, 0);
    }

    /**
     * get x coordinate of vector
     *
     * @return x coordinate
     */
    public float getX () {
        return this.floatBuffer.get(0);
    }

    /**
     * get y coordinate of vector
     *
     * @return y coordinate
     */
    public float getY () {
        return this.floatBuffer.get(1);
    }

    /**
     * get z coordinate of vector
     *
     * @return z coordinate
     */
    public float getZ () {
        return this.floatBuffer.get(2);
    }

    /**
     * set x coordinate of vector
     *
     * @param x x coordinate of vector
     */
    public void setX (final float x) {
        if (readonly.get()) {
            throw new IllegalStateException("vector is readonly.");
        }

        this.floatBuffer.put(0, x);
    }

    /**
     * set y coordinate of vector
     *
     * @param y y coordinate of vector
     */
    public void setY (final float y) {
        if (readonly.get()) {
            throw new IllegalStateException("vector is readonly.");
        }

        this.floatBuffer.put(1, y);
    }

    /**
     * set z coordinate of vector
     *
     * @param z z coordinate of vector
     */
    public void setZ (final float z) {
        if (readonly.get()) {
            throw new IllegalStateException("vector is readonly.");
        }

        this.floatBuffer.put(2, z);
    }

    /**
     * set x, y and z coordinates
     *
     * @param x x coordinate of vector
     * @param y y coordinate of vector
     * @param z z coordinate of vector
     */
    public void set (final float x, final float y, final float z) {
        if (readonly.get()) {
            throw new IllegalStateException("vector is readonly.");
        }

        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    /**
     * get absolute (betrag) of vector
     *
     * @return abs
     */
    public float abs () {
        //to set values to stack, get values first (performance optimization)
        final float x = getX();
        final float y = getY();
        final float z = getZ();

        //get absolute (betrag) of vector
        return (float) Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * normalize vector
     *
     * @link http://www.conitec.net/manual_d/avec-intro.htm
     */
    public void norm () {
        //calculate abs
        float abs = abs();

        //normalize values
        setX(1 / abs * getX());
        setY(1 / abs * getY());
        setZ(1 / abs * getZ());
    }

    /**
     * invert vector
     */
    public Vector3f invert() {
        setX(-getX());
        setY(-getY());
        setZ(-getZ());

        return this;
    }

    public void add (final Vector3f vector) {
        this.setX(getX() + vector.getX());
        this.setY(getY() + vector.getY());
        this.setZ(getZ() + vector.getZ());
    }

    public void add (final float x, final float y, final float z) {
        this.setX(getX() + x);
        this.setY(getY() + y);
        this.setZ(getZ() + z);
    }

    public void sub (final Vector3f vector) {
        this.add(vector.invert());
    }

    public void sub (final float x, final float y, final float z) {
        this.add(-x, -y, -z);
    }

    /**
     * scale vector
     *
     * @param factor scale factor
     */
    public void scale (float factor) {
        setX(getX() * factor);
        setY(getY() * factor);
        setZ(getZ() * factor);
    }

    /**
     * calculate cross product of 3 vectors
     *
     * @param vector vector
     *
     * @return cross product
     */
    public Vector3f crossProduct(final Vector3f vector) {
        return new Vector3f(
                getY() * vector.getZ() - getZ() * vector.getY(),
                getZ() * vector.getX() - getX() * vector.getZ(),
                getX() * vector.getY() - getY() * vector.getX());
    }

    /**
     * calculate the dotproduct (skalarprodukt / punktprodukt) of 3 vectors
     *
     * @param vector vector
     *
     * @return dot product
     */
    public float dotProduct(final Vector3f vector) {
        return getX() * vector.getX() + getY() * vector.getY() + getZ() * vector.getZ();
    }

    /**
     * get direct buffer
     *
     * @return instance of direct buffer
     */
    public FloatBuffer getDirectBuffer () {
        return this.floatBuffer.asReadOnlyBuffer();
    }

    /**
     * get direct byte buffer
     *
     * @return instance of direct byte buffer
     */
    public ByteBuffer getDirectByteBuffer () {
        return this.buffer.asReadOnlyBuffer();
    }

    /**
     * copy vector
     *
     * @return new vector object with same values
     */
    public Vector3f copy () {
        //create new vector with same values
        Vector3f vector = new Vector3f(getX(), getY(), getZ());

        return vector;
    }

    @Override
    public Vector3f clone () {
        return this.copy();
    }

    public boolean equals (Vector3f vector) {
        return this.getX() == vector.getX() && this.getY() == vector.getY() && this.getZ() == vector.getZ();
    }

    @Override
    public boolean equals (Object vector) {
        if (!(vector instanceof Vector2f)) {
            throw new UnsupportedOperationException("only vectors can be checked, if they are equals.");
        }

        return this.equals((Vector2f) vector);
    }

    public void setReadonly () {
        this.readonly.set(true);
    }

    public void setWriteable () {
        this.readonly.set(false);
    }

    public boolean isReadonly () {
        return this.readonly.get();
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
