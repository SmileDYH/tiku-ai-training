package com.edu.tiku.model.entity.jyw;

import java.util.UUID;

/// <summary>
/// 试题文件
/// </summary>
public  class QuesFile
{
    /// <summary>
    /// 序号
    /// </summary>
    public byte Seq;

    /// <summary>
    /// 试题标识
    /// </summary>
    public UUID QuesID;

    /// <summary>
    /// 文件名称
    /// </summary>
    public String Name;

    /// <summary>
    /// 视频或音频URL
    /// </summary>
    public String AudioUrl;
}