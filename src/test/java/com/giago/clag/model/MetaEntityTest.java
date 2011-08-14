package com.giago.clag.model;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;

public class MetaEntityTest {
	
	@Test
	public void stringClass() {
		assertEquals(MetaEntity.Type.STRING.getValue(), MetaEntity.getType(String.class));
	}

	@Test
	public void integerClass() {
		assertEquals(MetaEntity.Type.INTEGER.getValue(), MetaEntity.getType(Integer.class));
	}

	@Test
	public void longClass() {
		assertEquals(MetaEntity.Type.INTEGER.getValue(), MetaEntity.getType(Long.class));
	}
	
	@Test
	public void dateClass() {
		assertEquals(MetaEntity.Type.INTEGER.getValue(), MetaEntity.getType(Date.class));	
	}

	@Test
	public void listOfString() {
		assertEquals(MetaEntity.Type.STRING.getValue(), MetaEntity.getType(List.class));
	}

	@Test
	public void doubleClass() {
		assertEquals(MetaEntity.Type.REAL.getValue(), MetaEntity.getType(Double.class));
	}

	
}
