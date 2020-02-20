# InfectStatistic-221701214
疫情统计
<ul>
    <li>如何运行：<br />进入src文件夹<br />
    <code>javac -encoding UTF-8 InfectStatistic.java</code>
    <code><br />java InfectStastic -log D:\log\ -out D:\output.txt</code>
    </li>
    <br />
    <li>功能简介：<br />list命令 支持以下命令行参数：
        <ul>
            <li><code>-log</code> 指定日志目录的位置，该项<strong>必会附带</strong>。</li>
            <li><code>-out</code> 指定输出文件路径和文件名，该项<strong>必会附带</strong>。</li>
            <li><code>-date</code> 指定日期，你需要确保你处理了指定日期以及之前的所有log文件，不设置则默认为所提供日志最新的一天。</li>
            <li><code>-type</code> 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 <code>-type ip</code> 表示只列出感染患者的情况，<code>-type sp cure</code>则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。</li>
            <li><code>-province</code> 指定列出的省，如<code>-province 福建</code>，则只列出福建，<code>-province 全国 浙江</code>则只会列出全国、浙江，不指定则列出人数有变化的省份。</li>
        </ul>
    </li>
    <li>作业链接：<br /><a href="https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287">点击进入</a></li>
    <li>博客链接：<br /><a href="https://www.cnblogs.com/SIPUNK/p/12332717.html">点击进入</a></li>
</ul>