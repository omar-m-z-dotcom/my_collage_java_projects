package sham3aPackage;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;

public class driver implements Serializable {
	public String name;
	public String password;
	public String email;
	public long phone_number;
	public long National_id;
	public BufferedImage image;
	public ArrayList<String> favourite_areas;
}
