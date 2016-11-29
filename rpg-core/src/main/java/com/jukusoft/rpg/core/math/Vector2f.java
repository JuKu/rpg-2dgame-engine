package com.jukusoft.rpg.core.math;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * class for 2D vector with float values
 *
 * Created by Justin on 21.11.2016.
 */
public class Vector2f implements Cloneable, Serializable {

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
    public Vector2f(final float x, final float y) {
        //allocate new direct (Off Heap) byte buffer with size of 2 bytes
        this.buffer = ByteBuffer.allocateDirect(FLOAT_IN_BYTES * 2);

        //convert to float buffer to access data easely
        this.floatBuffer = this.buffer.asFloatBuffer();

        //set initial coordinates
        this.floatBuffer.put(0, x);
        this.floatBuffer.put(1, y);
    }

    /**
     * default constructor
     */
    public Vector2f() {
        //allocate new direct (Off Heap) byte buffer with size of 2 bytes
        this.buffer = ByteBuffer.allocateDirect(FLOAT_IN_BYTES * 2);

        //convert to float buffer to access data easely
        this.floatBuffer = this.buffer.asFloatBuffer();

        //set initial coordinates
        this.floatBuffer.put(0, 0);
        this.floatBuffer.put(1, 0);
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
    * set x and y coordinates
     *
     * @param x x coordinate of vector
     * @param y y coordinate of vector
    */
    public void set (final float x, final float y) {
        if (readonly.get()) {
            throw new IllegalStateException("vector is readonly.");
        }

        this.setX(x);
        this.setY(y);
    }

    /**
    * get absolute (betrag) of vector
     *
     * @return abs
    */
    public float abs () {
        //to set values to stack, get values first (performance optimization)
        float x = getX();
        float y = getY();

        //get absolute (betrag) of vector
        return (float) Math.sqrt((x * x) + (y * y));
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
        setX(1 / abs * getX()/* / abs*/);
        setY(1 / abs * getY()/* / abs*/);
    }

    /**
    * invert vector
    */
    public Vector2f invert () {
        setX(-getX());
        setY(-getY());

        return this;
    }

    public void add (final Vector2f vector) {
        this.setX(getX() + vector.getX());
        this.setY(getY() + vector.getY());
    }

    public void add (final float x, final float y) {
        this.setX(getX() + x);
        this.setY(getY() + y);
    }

    public void sub (final Vector2f vector) {
        this.add(vector.invert());
    }

    public void sub (final float x, final float y) {
        this.add(-x, -y);
    }

    /*public void mul (Vector2f vector) {
        //get coordinates, all values are final, so JVM can optimize memory access
        final float x1 = getX();
        final float y1 = getY();
        final float x2 = vector.getX();
        final float y2 = vector.getY();

        //calculate new x and y values#
        setX();
    }*/

    /**
    * scale vector
     *
     * @param factor scale factor
    */
    public void scale (float factor) {
        setX(getX() * factor);
        setY(getY() * factor);
    }

    /**
     * NOTE: Justin did some shit here.
    * calculate cross product of 2 vectors
     *
     * @param vector vector
     *
     * @return cross product
    
    public float crossProduct (final Vector2f vector) {
        return getX() * vector.getY() - vector.getX() * getY();
    }*/

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
    public Vector2f copy () {
        //create new vector with same values
        Vector2f vector = new Vector2f(getX(), getY());

        return vector;
    }

    @Override
    public Vector2f clone () {
        return this.copy();
    }

    public boolean equals (Vector2f vector) {
        return this.getX() == vector.getX() && this.getY() == vector.getY();
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
