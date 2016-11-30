package com.jukusoft.rpg.graphic.opengl.text;

import com.jukusoft.rpg.core.utils.ArrayUtils;
import com.jukusoft.rpg.graphic.opengl.font.CharacterInfo;
import com.jukusoft.rpg.graphic.opengl.font.FontTexture;
import com.jukusoft.rpg.graphic.opengl.mesh.Material;
import com.jukusoft.rpg.graphic.opengl.mesh.Mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 30.11.2016.
 */
public class TextMeshFactory {

    private static final float ZPOS = 0.0f;

    private static final int VERTICES_PER_QUAD = 4;

    public static Mesh createMesh (FontTexture fontTexture, String text) {
        List<Float> positions = new ArrayList();
        List<Float> textCoords = new ArrayList();

        //we dont need normals for text
        float[] normals   = new float[0];

        //indices
        List<Integer> indices   = new ArrayList();

        //convert text to character array and get length
        char[] chars = text.toCharArray();
        int numChars = chars.length;
        float startX = 0;

        //iterate through all chars of text
        for (int i = 0; i < numChars; i++) {
            //get character with inde
            char c = chars[i];

            //get CharacterInfo of char
            CharacterInfo charInfo = fontTexture.getCharacterInfo(c);

            //use 2 triangles to create tile for one character

            //left top vertex
            positions.add(startX); //x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add( (float)charInfo.getStartX() / (float)fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i * VERTICES_PER_QUAD);

            //left bottom vertex
            positions.add(startX); // x
            positions.add((float)fontTexture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float)charInfo.getStartX() / (float)fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i * VERTICES_PER_QUAD + 1);

            //right bottom vertex
            positions.add(startX + charInfo.getWidth()); // x
            positions.add((float)fontTexture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 2);

            //right top vertex
            positions.add(startX + charInfo.getWidth()); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD + 3);

            // Add indices por left top and bottom right vertices
            indices.add(i*VERTICES_PER_QUAD);
            indices.add(i*VERTICES_PER_QUAD + 2);

            //add width of character to startX
            startX += charInfo.getWidth();
        }

        float[] posArr = ArrayUtils.convertFloatListToArray(positions);
        float[] textCoordsArr = ArrayUtils.convertFloatListToArray(textCoords);

        int[] indicesArr = indices.stream().mapToInt((i) -> {
            return i;
        }).toArray();

        //create new mesh with material
        Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
        mesh.setMaterial(new Material(fontTexture.getTexture()));

        return mesh;
    }

}
