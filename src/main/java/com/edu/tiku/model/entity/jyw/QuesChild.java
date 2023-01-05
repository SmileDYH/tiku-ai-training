package com.edu.tiku.model.entity.jyw;

import java.util.UUID;

public class QuesChild {
	/// <summary>
	/// 序号
	/// </summary>
	public byte Seq;

	/// <summary>
	/// 试题标识
	/// </summary>
	public UUID QuesID;

	/// <summary>
	/// 子题标识
	/// </summary>
	public UUID ChildID;

	/// <summary>
	/// 子题分值
	/// </summary>
	public Float Score;
}
