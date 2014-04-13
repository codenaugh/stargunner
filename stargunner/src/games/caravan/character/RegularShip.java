package games.caravan.character;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

public class RegularShip extends Ship {
	
	private static float[] vrts = new float[] {0,1,0,-1,-1,1,1,-1,1,1,-1,-1,-1,-1,-1};
	private static float[] cl = new float[] {1,0,0,1,0,1,0,1,0,0,1,1,1,1,0,1,1,0,1,1};
	private static int[] triangles = new int[] {0,1,2,0,2,3,0,3,4,0,4,1,1,4,2,4,3,2};

	public RegularShip() {
		super();
		FloatBuffer vertBuf =
				com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts);
				FloatBuffer colorBuf =
				com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl);
				IntBuffer triangleBuf =
				com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles);
				this.setVertexBuffer(vertBuf);
				this.setColorBuffer(colorBuf);
				this.setIndexBuffer(triangleBuf);
				this.rotate(90, new Vector3D(1,0,0));
	}

	public RegularShip(Point3D p) {
		super(p);
		FloatBuffer vertBuf =
				com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts);
				FloatBuffer colorBuf =
				com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl);
				IntBuffer triangleBuf =
				com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles);
				this.setVertexBuffer(vertBuf);
				this.setColorBuffer(colorBuf);
				this.setIndexBuffer(triangleBuf);
				this.rotate(90, new Vector3D(1,0,0));
	}

	@Override
	public void fire() {
		

	}

}
