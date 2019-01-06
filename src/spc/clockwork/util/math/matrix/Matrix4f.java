package spc.clockwork.util.math.matrix;


import spc.clockwork.gameobject.Camera;
import spc.clockwork.gameobject.GameObject;
import spc.clockwork.util.math.vector.FloatVector;
import spc.clockwork.util.math.vector.Vector3f;

/**
 * Matrix4f is a developer-friendly class, which is used to manipulate with 3d space objects
 * Matrix4f stores a bunch of pre-defined matrices that are mandatory for working with 3d graphics
 *
 * @author wize
 * @version 1 (2018.04.25)
 */
public final class Matrix4f extends FloatMatrix {


    /* ATTRIBUTES
    /*--------------------*/
    private static final String ERROR_INVALID_ELEMENTS_ARRAY =
            "Error: Invalid elements array in Matrix4f construction (float[4][4] is required)";


    public static final Matrix4f IDENTITY_MATRIX = Matrix4f.newIdentityMatrix();
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Reconstructs {@link Matrix4f} from {@link FloatMatrix}
     * @param matrix {@link FloatMatrix}
     * @return {@link Matrix4f}
     */
    private static Matrix4f reconstructFromFloatMatrix(FloatMatrix matrix) {
        return new Matrix4f(matrix.elements);
    }


    /**
     * An identity matrix4f
     * @return new identity matrix4f
     */
    public static Matrix4f newIdentityMatrix() {
        return newScalarMatrix(1);
    }


    /**
     * A diagonal matrix4f, which was scaled by an argument
     *
     * @param scalar scalar multiplier
     * @return new scalar matrix4f
     *
     */
    public static Matrix4f newScalarMatrix(float scalar) {
        return new Matrix4f(
                new float[][]{
                        {scalar, 0f, 0f, 0f},
                        {0f, scalar, 0f, 0f},
                        {0f, 0f, scalar, 0f},
                        {0f, 0f, 0f, scalar},
                }
        );
    }


    /**
     * A Translation Matrix
     *
     * @param x the x coordinate of translation
     * @param y the y coordinate of translation
     * @param z the z coordinate of translation
     * @return new Translation matrix4f
     *
     */
    public static Matrix4f newTranslationMatrix(float x, float y, float z) {
        return new Matrix4f(
                new float[][]{
                        {1f, 0f, 0f,  x},
                        {0f, 1f, 0f,  y},
                        {0f, 0f, 1f,  z},
                        {0f, 0f, 0f, 1f}
                }
        );
    }


    /**
     * A Translation Matrix
     *
     * @param vector vector of translation
     * @return new Translation matrix4f
     *
     */
    public static Matrix4f newTranslationMatrix(Vector3f vector) {
        return newTranslationMatrix(vector.x(), vector.y(), vector.z());
    }


    /**
     * A rotation matrix around the x-axis
     * @param rotationAngleX an x-rotation angle in radians
     * @return a new rotation around the x-axis matrix4f
     */
    public static Matrix4f newRotationMatrixX(float rotationAngleX) {
        return new Matrix4f(
                new float[][]{
                        {1f,                               0f,                                0f, 0f},
                        {0f, (float) Math.cos(rotationAngleX), -(float) Math.sin(rotationAngleX), 0f},
                        {0f, (float) Math.sin(rotationAngleX),  (float) Math.cos(rotationAngleX), 0f},
                        {0f,                               0f,                                0f, 1f}
                }
        );
    }


    /**
     * A rotation matrix around the y-axis
     * @param rotationAngleY an y-rotation angle in radians
     * @return a new rotation around the y-axis matrix4f
     */
    public static Matrix4f newRotationMatrixY(float rotationAngleY) {
        return new Matrix4f(
                new float[][]{
                        { (float) Math.cos(rotationAngleY), 0f, (float) Math.sin(rotationAngleY), 0f},
                        {                               0f, 1f,                               0f, 0f},
                        {-(float) Math.sin(rotationAngleY), 0f, (float) Math.cos(rotationAngleY), 0f},
                        {                               0f, 0f,                               0f, 1f}
                }
        );
    }


    /**
     * A rotation matrix around the z-axis
     * @param rotationAngleZ an z-rotation angle in radians
     * @return a new rotation around the z-axis matrix4f
     */
    public static Matrix4f newRotationMatrixZ(float rotationAngleZ) {
        return new Matrix4f(
                new float[][]{
                        {(float) Math.cos(rotationAngleZ), -(float) Math.sin(rotationAngleZ), 0f, 0f},
                        {(float) Math.sin(rotationAngleZ),  (float) Math.cos(rotationAngleZ), 0f, 0f},
                        {                              0f,                                0f, 1f, 0f},
                        {                              0f,                                0f, 0f, 1f}
                }
        );
    }


    /**
     * A rotation matrix ZXY
     * @param x rotation around x-axis in radians
     * @param y rotation around y-axis in radians
     * @param z rotation around z-axis in radians
     * @return a new rotation matrix
     */
    public static Matrix4f newRotationMatrixZXY(float x, float y, float z) {
        //return newRotationMatrixZ(z).mul(newRotationMatrixX(x)).mul(newRotationMatrixY(y));
        return newRotationMatrixZ(z).mul(newRotationMatrixX(x).mul(newRotationMatrixY(y)));
    }


    /**
     * A rotation matrix ZXY
     * @param vector a rotation vector in radians
     * @return a new rotation matrix
     */
    public static Matrix4f newRotationMatrixZXY(Vector3f vector) {
        return newRotationMatrixZXY(vector.x(), vector.y(), vector.z());
    }


    /**
     * A rotation matrix YXZ
     * @param x rotation around x-axis in radians
     * @param y rotation around y-axis in radians
     * @param z rotation around z-axis in radians
     * @return a new rotation matrix
     */
    public static Matrix4f newRotationMatrixYXZ(float x, float y, float z) {
        return newRotationMatrixY(y).mul(newRotationMatrixX(x).mul(newRotationMatrixZ(z)));
    }


    /**
     * A rotation matrix ZXY
     * @param vector a rotation vector in radians
     * @return a new rotation matrix
     */
    public static Matrix4f newRotationMatrixYXZ(Vector3f vector) {
        return newRotationMatrixYXZ(vector.x(), vector.y(), vector.z());
    }


    /**
     * A scale matrix
     * @param x scale relative to x-axis
     * @param y scale relative to y-axis
     * @param z scale relative to z-axis
     * @return a new scale matrix4f
     */
    public static Matrix4f newScaleMatrix(float x, float y, float z) {
        return new Matrix4f(
                new float[][]{
                        { x, 0f, 0f, 0f},
                        {0f,  y, 0f, 0f},
                        {0f, 0f,  z, 0f},
                        {0f, 0f, 0f, 1f},
                }
        );
    }


    /**
     * A scale matrix
     * @param vector a vector of scale
     * @return a new scale matrix4f
     */
    public static Matrix4f newScaleMatrix(Vector3f vector) {
        return newScaleMatrix(vector.x(), vector.y(), vector.z());
    }

    /**
     * A scale matrix
     * @param scale the scale factor
     * @return a new scale matrix4f
     */
    public static Matrix4f newScaleMatrix(float scale) {
        return newScaleMatrix(scale, scale, scale);
    }


    /**
     * Creates a model matrix from the gameObject
     * Model matrix is a combination of transformation matrices
     * @deprecated
     * @see GameObject::getAbsoluteModelMatrix()
     * @param gameObject the {@link GameObject}
     * @return a model view matrix of this game object
     */
    public static Matrix4f newModelMatrix(GameObject gameObject) {
        return newTranslationMatrix(gameObject.getPosition())
                .mul(gameObject.getRotation().toRotationMatrix())
                .mul(newScaleMatrix(gameObject.getScale()));
    }


    /**
     * Creates a view matrix from the camera
     * @deprecated
     * @see Camera::getAbsoluteViewMatrix()
     * @param camera a camera, from which perspective we observe.
     * @return a new view matrix
     */
    public static Matrix4f newViewMatrix(Camera camera) {
        return camera.getAbsoluteViewMatrix();
        // return newRotationMatrixZXY(camera.getRotationInRadians().negate())
        //         .mul(newTranslationMatrix(camera.getPosition().negate()));
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/


    /* ----- BEGIN OF THE COMPUTER GRAPHICS API -----*/

    /**
     * A projection matrix
     * @param aspectRatio a ratio between width and height of the screen
     * @param fieldOfView the field of view in radians
     * @param zNear       the nearest point the camera can observe
     * @param zFar        the farthest pint the camera can observe
     * @return a new projection matrix
     */
    public static Matrix4f newPerspectiveProjectionMatrix(float aspectRatio,
                                                          float fieldOfView,
                                                          float zNear,
                                                          float zFar) {
        float tanHalfFOV = (float)Math.tan(fieldOfView/2);
        float zRange = zFar - zNear;
        float zSum = zFar + zNear;

        float m00 = (1f / tanHalfFOV) / aspectRatio;
        float m11 = 1f / tanHalfFOV;
        float m22 = -zRange / zSum;
        float m23 = -1f;
        float m32 = -(2 * zFar * zNear) / zRange;

        return new Matrix4f(
                new float[][] {
                        {m00,  0f,  0f,  0f},
                        { 0f, m11,  0f,  0f},
                        { 0f,  0f, m22, m32},
                        { 0f,  0f, m23,  0f}
                }
        );
    }


    /**
     * Creates a model view matrix -- a matrix which shows the object from camera perspective
     * @param camera a camera, from which perspective we observe.
     * @param gameObject {@link GameObject}, which we observe
     * @return a new model view matrix
     */
    public static Matrix4f newModelViewMatrix(Camera camera, GameObject gameObject) {
        return camera.getAbsoluteViewMatrix().mul(gameObject.getAbsoluteModelMatrix());
    }


    /**
     * Orthographic projection matrix is used to project objects on the screen plane, ignoring the Z-axis
     * This matrix is mostly used in the HUD layers
     * @param left leftmost visible coordinate
     * @param right rightmost visible coordinate
     * @param bottom the most bottom visible coordinate
     * @param top the highest visible coordinate
     * @param zFar the Z position of a far plane
     * @param zNear the Z position of a near plane
     * @return a new instance of {@link Matrix4f} that does the required orthographic projection
     */
    public static Matrix4f newOrthographicProjectionMatrix(float left,
                                                           float right,
                                                           float bottom,
                                                           float top,
                                                           float zFar,
                                                           float zNear) {
        float m00 = 2 / (right - left);
        float m11 = 2 / (top - bottom);
        float m22 = -2 / (zFar - zNear);
        float m30 = - (right + left) / (right - left);
        float m31 = - (top + bottom) / (top - bottom);
        float m32 = - (zFar + zNear) / (zFar - zNear);
        return new Matrix4f(new float[][] {
                {m00, 0f,  0f,  m30},
                {0f,  m11, 0f,  m31},
                {0f,  0f,  m22, m32},
                {0f,  0f,  0f,  1f }
        });
    }

    /* ----- END OF THE COMPUTER GRAPHICS API -----*/



    @Override
    public Matrix4f scale(float scalar) {
        return new Matrix4f(super.scale(scalar));
    }

    @Override
    public Matrix4f negate() {
        return new Matrix4f(super.negate());
    }

    @Override
    public Matrix4f add(FloatMatrix matrix) {
        return new Matrix4f(super.add(matrix));
    }

    @Override
    public Matrix4f mul(FloatMatrix matrix) {
        return new Matrix4f(super.mul(matrix));
    }

    @Override
    public Matrix4f transpose() {
        return new Matrix4f(super.transpose());
    }

    @Override
    public Matrix4f invert() {
        return new Matrix4f(super.invert());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mat4[");
        stringBuilder.append(this.getElement(0,0));
        stringBuilder.append("\t");
        stringBuilder.append(this.getElement(0,1));
        stringBuilder.append("\t");
        stringBuilder.append(this.getElement(0,2));
        stringBuilder.append("\t");
        stringBuilder.append(this.getElement(0,3));
        stringBuilder.append("]");
        stringBuilder.append("\n");

        stringBuilder.append("    [");
        stringBuilder.append(this.getElement(1,0));
        stringBuilder.append("\t");
        stringBuilder.append(this.getElement(1,1));
        stringBuilder.append("\t");
        stringBuilder.append(this.getElement(1,2));
        stringBuilder.append("\t");
        stringBuilder.append(this.getElement(1,3));
        stringBuilder.append("]");
        stringBuilder.append("\n");

        stringBuilder.append("    [");
        stringBuilder.append(this.getElement(2,0));
        stringBuilder.append("\t");
        stringBuilder.append(this.getElement(2,1));
        stringBuilder.append("\t");
        stringBuilder.append(this.getElement(2,2));
        stringBuilder.append("\t");
        stringBuilder.append(this.getElement(2,3));
        stringBuilder.append("]");
        stringBuilder.append("\n");

        stringBuilder.append("    [");
        stringBuilder.append(this.getElement(3,0));
        stringBuilder.append("\t");
        stringBuilder.append(this.getElement(3,1));
        stringBuilder.append("\t");
        stringBuilder.append(this.getElement(3,2));
        stringBuilder.append("\t");
        stringBuilder.append(this.getElement(3,3));
        stringBuilder.append("]");
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }


    /**
     * Multiplies a matrix by a vector, obtaining a new vector
     * @param vector the 3d vector to multiply with
     * @param w the 4-th dimensional component
     * @return the xyz components of the 4d result
     */
    public Vector3f mul(Vector3f vector, float w) {
        FloatVector vec = super.mul(new FloatVector(new float[] {vector.x(), vector.y(), vector.z(), w}));
        return new Vector3f(vec.getElement(0), vec.getElement(1), vec.getElement(2));
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A constrictor that creates a 4*4 matrix with predefined elements
     * @param elements an array of elements of the matrix
     */
    public Matrix4f(float[][] elements) {
        super(elements);
        if (elements.length != 4 || elements[0].length != 4)
            throw new IndexOutOfBoundsException(ERROR_INVALID_ELEMENTS_ARRAY);
    }


    /**
     * A classic constructor that creates a 4*4 zero-matrix
     * Note: Completely useless
     * @deprecated
     */
    public Matrix4f() {
        super(new float[][]{
                {0f, 0f, 0f, 0f},
                {0f, 0f, 0f, 0f},
                {0f, 0f, 0f, 0f},
                {0f, 0f, 0f, 0f}});
    }


    /**
     * An "auto-casting" constructor
     *
     * @param matrix a matrix to reconstruct
     *
     */
    private Matrix4f(FloatMatrix matrix) {
        super(matrix.elements);
    }

    /*--------------------*/
}
