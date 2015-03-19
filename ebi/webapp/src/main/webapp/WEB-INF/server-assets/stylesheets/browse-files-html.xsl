<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ae="http://www.ebi.ac.uk/arrayexpress/xslt"
                xmlns:html="http://www.w3.org/1999/xhtml"
                extension-element-prefixes="ae html"
                exclude-result-prefixes="ae html"
                version="2.0">

    <xsl:param name="accession"/>
    <xsl:param name="kind"/>
    <xsl:param name="userid"/>

    <!-- dynamically set by QueryServlet: host name (as seen from client) and base context path of webapp -->
    <xsl:param name="host"/>
    <xsl:param name="basepath"/>

    <xsl:include href="ae-file-functions.xsl"/>

    <xsl:variable name="vBaseUrl">http://<xsl:value-of select="$host"/><xsl:value-of select="$basepath"/></xsl:variable>   
    
    <xsl:variable name="vAccession" select="upper-case($accession)"/>
    <xsl:variable name="vFilesDoc" select="doc('files.xml')"/>

    <xsl:output omit-xml-declaration="yes" method="html" indent="no" encoding="ISO-8859-1" />

    <xsl:include href="ae-html-page.xsl"/>

    <xsl:template match="/experiments">
        <html lang="en">
            <xsl:call-template name="page-header">
                <xsl:with-param name="pTitle">Files for <xsl:value-of select="$vAccession"/> | ArrayExpress Archive | EBI</xsl:with-param>
                <xsl:with-param name="pExtraCode">
                    <link rel="stylesheet" href="{$basepath}/assets/stylesheets/ae_common-1.4.css" type="text/css"/>
                    <link rel="stylesheet" href="{$basepath}/assets/stylesheets/ae_files-1.4.css" type="text/css"/>
                </xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="page-body"/>
        </html>
    </xsl:template>

    <xsl:template name="ae-contents">
        <xsl:variable name="vExperiment" select="experiment[accession=$vAccession]"/>
        <div class="ae_left_container_100pc assign_font">
            <div id="ae_files_content">
                <xsl:choose>
                    <xsl:when test="$vExperiment">
                        <xsl:variable name="vUserMatch" select="$vExperiment/user[text() = $userid]"/>
                        <xsl:choose>
                            <xsl:when test="not($userid) or $vUserMatch">
                                <div class="ae_accession">Experiment <xsl:value-of select="$vAccession"/></div>
                                <div class="ae_title"><xsl:value-of select="$vExperiment/name"/></div>
                                <xsl:variable name="vExperimentFiles" select="$vFilesDoc/files/folder[@accession = $vAccession]/file"/>
                                <xsl:call-template name="files-for-accession">
                                    <xsl:with-param name="pAccession" select="$vAccession"/>
                                    <xsl:with-param name="pFiles" select="$vExperimentFiles"/>
                                </xsl:call-template>
                                <xsl:variable name="vComment">
                                    <xsl:if test="($kind='raw' or $kind='fgem') and count($vExperimentFiles[@kind=$kind]) > 0">
                                        <div>
                                            <xsl:text>Due to the large amount of data there are multiple archive files for download.</xsl:text>
                                        </div>
                                    </xsl:if>
                                    <xsl:if test="($vExperiment/user = '1') and (count($vExperiment/file[size>2048000000])>0)">
                                        <div>
                                            <xsl:text>Some files are larger that 2 GB, please use </xsl:text>
                                            <a href="ftp://ftp.ebi.ac.uk/pub/databases/microarray/data/experiment/{substring($vAccession, 3, 4)}/{$vAccession}">ArrayExpress FTP</a>
                                            <xsl:text> to download these.</xsl:text>
                                        </div>
                                    </xsl:if>
                                </xsl:variable>
                                <xsl:if test="string-length($vComment)>0">
                                    <div class="ae_comment"><xsl:copy-of select="$vComment"/></div>
                                </xsl:if>

                                <xsl:if test="$kind='' or $kind='all'">
                                    <xsl:for-each select="$vExperiment/arraydesign">
                                        <xsl:sort select="accession" order="ascending"/>
                                        <xsl:variable name="vArrayAccession" select="string(accession)"/>
                                        <div class="ae_accession">Array Design <xsl:value-of select="$vArrayAccession"/></div>
                                        <div class="ae_title"><xsl:value-of select="name"/></div>
                                        <xsl:call-template name="files-for-accession">
                                            <xsl:with-param name="pAccession" select="$vArrayAccession"/>
                                            <xsl:with-param name="pFiles" select="$vFilesDoc/files/folder[@accession = $vArrayAccession]/file"/>
                                        </xsl:call-template>
                                    </xsl:for-each>
                                </xsl:if>
                            </xsl:when>
                            <xsl:otherwise>
                                <div id="ae_infotext">The access to experiment data for <xsl:value-of select="$vAccession"/> is restricted.</div>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <div id="ae_infotext">The experiment with accession <xsl:value-of select="$vAccession"/> is not present in the archive.</div>
                    </xsl:otherwise>
                </xsl:choose>
            </div>
        </div>
    </xsl:template>

    <xsl:template name="files-for-accession">
        <xsl:param name="pAccession"/>
        <xsl:param name="pFiles"/>
        <xsl:variable name="vKind"><xsl:if test="$kind!='all'"><xsl:value-of select="$kind"/></xsl:if></xsl:variable>

        <table class="ae_files_table" border="0" cellpadding="0" cellspacing="0">
            <tbody>
                <xsl:if test="not($pFiles[$vKind='' or @kind=$vKind])">
                    <tr><td class="td_all" colspan="3"><div>No files</div></td></tr>
                </xsl:if>
                <xsl:for-each select="$pFiles[$vKind='' or @kind=$vKind]">
                    <xsl:sort select="contains(lower-case(@name), 'readme')" order="descending"/>
                    <xsl:sort select="@kind='raw' or @kind='fgem'" order="descending"/>
                    <xsl:sort select="@kind='adf' or @kind='idf' or @kind='sdrf'" order="descending"/>
                    <xsl:sort select="lower-case(@name)" order="ascending"/>
                    <tr>
                        <td class="td_name"><a href="{$vBaseUrl}/files/{$pAccession}/{@name}"><xsl:value-of select="@name"/></a></td>
                        <td class="td_size"><xsl:value-of select="ae:formatfilesize(@size)"/></td>
                        <td class="td_date"><xsl:value-of select="@lastmodified"/></td>
                    </tr>
                </xsl:for-each>
            </tbody>
        </table>
    </xsl:template>

</xsl:stylesheet>
