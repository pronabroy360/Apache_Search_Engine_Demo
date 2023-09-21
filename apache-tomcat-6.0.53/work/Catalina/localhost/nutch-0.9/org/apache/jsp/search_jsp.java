package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.*;
import java.util.*;
import java.net.*;
import org.apache.nutch.html.Entities;
import org.apache.nutch.metadata.Nutch;
import org.apache.nutch.searcher.*;
import org.apache.nutch.plugin.*;
import org.apache.nutch.clustering.*;
import org.apache.hadoop.conf.*;
import org.apache.nutch.util.NutchConfiguration;

public final class search_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(3);
    _jspx_dependants.add("/more.jsp");
    _jspx_dependants.add("/cluster.jsp");
    _jspx_dependants.add("/WEB-INF/taglibs-i18n.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fi18n_005fbundle_0026_005fbaseName_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fi18n_005fmessageArg_0026_005fvalue_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fi18n_005fbundle_0026_005fbaseName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fi18n_005fmessageArg_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fi18n_005fbundle_0026_005fbaseName_005fnobody.release();
    _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey.release();
    _005fjspx_005ftagPool_005fi18n_005fmessageArg_0026_005fvalue_005fnobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, false, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\n');

  Configuration nutchConf = NutchConfiguration.get(application);
  
  /**
   * Number of hits to retrieve and cluster if clustering extension is available
   * and clustering is on. By default, 100. Configurable via nutch-conf.xml.
   */
  int HITS_TO_CLUSTER = 
    nutchConf.getInt("extension.clustering.hits-to-cluster", 100);

  /**
   * An instance of the clustering extension, if available.
   */
  OnlineClusterer clusterer = null;
  try {
    clusterer = new OnlineClustererFactory(nutchConf).getOnlineClusterer();
  } catch (PluginRuntimeException e) {
    // NOTE: Dawid Weiss
    // should we ignore plugin exceptions, or rethrow it? Rethrowing
    // it effectively prevents the servlet class from being loaded into
    // the JVM
  }
  

      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');

  NutchBean bean = NutchBean.get(application, nutchConf);
  // set the character encoding to use when interpreting request values 
  request.setCharacterEncoding("UTF-8");

  bean.LOG.info("query request from " + request.getRemoteAddr());

  // get query from request
  String queryString = request.getParameter("query");
  if (queryString == null)
    queryString = "";
  String htmlQueryString = Entities.encode(queryString);
  
  // a flag to make the code cleaner a bit.
  boolean clusteringAvailable = (clusterer != null);

  String clustering = "";
  if (clusteringAvailable && "yes".equals(request.getParameter("clustering")))
    clustering = "yes";

  int start = 0;          // first hit to display
  String startString = request.getParameter("start");
  if (startString != null)
    start = Integer.parseInt(startString);

  int hitsPerPage = 10;          // number of hits to display
  String hitsString = request.getParameter("hitsPerPage");
  if (hitsString != null)
    hitsPerPage = Integer.parseInt(hitsString);

  int hitsPerSite = 2;                            // max hits per site
  String hitsPerSiteString = request.getParameter("hitsPerSite");
  if (hitsPerSiteString != null)
    hitsPerSite = Integer.parseInt(hitsPerSiteString);

  String sort = request.getParameter("sort");
  boolean reverse =
    sort!=null && "true".equals(request.getParameter("reverse"));

  String params = "&hitsPerPage="+hitsPerPage
     +(sort==null ? "" : "&sort="+sort+(reverse?"&reverse=true":""));

  int hitsToCluster = HITS_TO_CLUSTER;            // number of hits to cluster

  // get the lang from request
  String queryLang = request.getParameter("lang");
  if (queryLang == null) { queryLang = ""; }
  Query query = Query.parse(queryString, queryLang, nutchConf);
  bean.LOG.info("query: " + queryString);
  bean.LOG.info("lang: " + queryLang);

  String language =
    ResourceBundle.getBundle("org.nutch.jsp.search", request.getLocale())
    .getLocale().getLanguage();
  String requestURI = HttpUtils.getRequestURL(request).toString();
  String base = requestURI.substring(0, requestURI.lastIndexOf('/'));
  String rss = "../opensearch?query="+htmlQueryString
    +"&hitsPerSite="+hitsPerSite+"&lang="+queryLang+params;

      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n");

  // To prevent the character encoding declared with 'contentType' page
  // directive from being overriden by JSTL (apache i18n), we freeze it
  // by flushing the output buffer. 
  // see http://java.sun.com/developer/technicalArticles/Intl/MultilingualJSP/
  out.flush();

      out.write('\n');
      out.write('\n');
      if (_jspx_meth_i18n_005fbundle_005f0(_jspx_page_context))
        return;
      out.write("\n");
      out.write("<html lang=\"");
      out.print( language );
      out.write("\">\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n");
      out.write("<head>\n");
      out.write("<title>Nutch: ");
      if (_jspx_meth_i18n_005fmessage_005f0(_jspx_page_context))
        return;
      out.write("</title>\n");
      out.write("<link rel=\"icon\" href=\"img/favicon.ico\" type=\"image/x-icon\"/>\n");
      out.write("<link rel=\"shortcut icon\" href=\"img/favicon.ico\" type=\"image/x-icon\"/>\n");
      out.write("<link rel=\"alternate\" type=\"application/rss+xml\" title=\"RSS\" href=\"");
      out.print(rss);
      out.write("\"/>\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "include/style.html", out, false);
      out.write("\n");
      out.write("<base href=\"");
      out.print( base  + "/" + language );
      out.write("/\">\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("<!--\n");
      out.write("function queryfocus() { document.search.query.focus(); }\n");
      out.write("// -->\n");
      out.write("</script>\n");
      out.write("</head>\n");
      out.write("\n");
      out.write("<body onLoad=\"queryfocus();\">\n");
      out.write("\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response,  language + "/include/header.html", out, false);
      out.write("\n");
      out.write("\n");
      out.write(" <form name=\"search\" action=\"../search.jsp\" method=\"get\">\n");
      out.write(" <input name=\"query\" size=44 value=\"");
      out.print(htmlQueryString);
      out.write("\">\n");
      out.write(" <input type=\"hidden\" name=\"hitsPerPage\" value=\"");
      out.print(hitsPerPage);
      out.write("\">\n");
      out.write(" <input type=\"hidden\" name=\"lang\" value=\"");
      out.print(language);
      out.write("\">\n");
      out.write(" <input type=\"submit\" value=\"");
      if (_jspx_meth_i18n_005fmessage_005f1(_jspx_page_context))
        return;
      out.write("\">\n");
      out.write(" ");
 if (clusteringAvailable) { 
      out.write("\n");
      out.write("   <input id=\"clustbox\" type=\"checkbox\" name=\"clustering\" value=\"yes\" ");
 if (clustering.equals("yes")) { 
      out.write("CHECKED");
 } 
      out.write(">\n");
      out.write("    <label for=\"clustbox\">");
      if (_jspx_meth_i18n_005fmessage_005f2(_jspx_page_context))
        return;
      out.write("</label>\n");
      out.write(" ");
 } 
      out.write("\n");
      out.write(" <a href=\"help.html\">help</a>\n");
      out.write(" </form>\n");
      out.write("\n");
      out.write('\n');
      out.write('\n');

   // how many hits to retrieve? if clustering is on and available,
   // take "hitsToCluster", otherwise just get hitsPerPage
   int hitsToRetrieve = (clusteringAvailable && clustering.equals("yes") ? hitsToCluster : hitsPerPage);

   if (clusteringAvailable && clustering.equals("yes")) {
     bean.LOG.info("Clustering is on, hits to retrieve: " + hitsToRetrieve);
   }

   // perform query
    // NOTE by Dawid Weiss:
    // The 'clustering' window actually moves with the start
    // position.... this is good, bad?... ugly?....
   Hits hits;
   try{
     hits = bean.search(query, start + hitsToRetrieve, hitsPerSite, "site",
                        sort, reverse);
   } catch (IOException e){
     hits = new Hits(0,new Hit[0]);	
   }
   int end = (int)Math.min(hits.getLength(), start + hitsPerPage);
   int length = end-start;
   int realEnd = (int)Math.min(hits.getLength(), start + hitsToRetrieve);

   Hit[] show = hits.getHits(start, realEnd-start);
   HitDetails[] details = bean.getDetails(show);
   Summary[] summaries = bean.getSummary(details, query);
   bean.LOG.info("total hits: " + hits.getTotal());

      out.write('\n');
      out.write('\n');
      //  i18n:message
      org.apache.taglibs.i18n.MessageTag _jspx_th_i18n_005fmessage_005f3 = (org.apache.taglibs.i18n.MessageTag) _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey.get(org.apache.taglibs.i18n.MessageTag.class);
      boolean _jspx_th_i18n_005fmessage_005f3_reused = false;
      try {
        _jspx_th_i18n_005fmessage_005f3.setPageContext(_jspx_page_context);
        _jspx_th_i18n_005fmessage_005f3.setParent(null);
        // /search.jsp(201,0) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
        _jspx_th_i18n_005fmessage_005f3.setKey("hits");
        int _jspx_eval_i18n_005fmessage_005f3 = _jspx_th_i18n_005fmessage_005f3.doStartTag();
        if (_jspx_eval_i18n_005fmessage_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          if (_jspx_eval_i18n_005fmessage_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
            out = org.apache.jasper.runtime.JspRuntimeLibrary.startBufferedBody(_jspx_page_context, _jspx_th_i18n_005fmessage_005f3);
          }
          do {
            out.write('\n');
            out.write(' ');
            out.write(' ');
            //  i18n:messageArg
            org.apache.taglibs.i18n.MessageArgumentTag _jspx_th_i18n_005fmessageArg_005f0 = (org.apache.taglibs.i18n.MessageArgumentTag) _005fjspx_005ftagPool_005fi18n_005fmessageArg_0026_005fvalue_005fnobody.get(org.apache.taglibs.i18n.MessageArgumentTag.class);
            boolean _jspx_th_i18n_005fmessageArg_005f0_reused = false;
            try {
              _jspx_th_i18n_005fmessageArg_005f0.setPageContext(_jspx_page_context);
              _jspx_th_i18n_005fmessageArg_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_i18n_005fmessage_005f3);
              // /search.jsp(202,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_i18n_005fmessageArg_005f0.setValue(new Long((end==0)?0:(start+1)));
              int _jspx_eval_i18n_005fmessageArg_005f0 = _jspx_th_i18n_005fmessageArg_005f0.doStartTag();
              if (_jspx_th_i18n_005fmessageArg_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                return;
              }
              _005fjspx_005ftagPool_005fi18n_005fmessageArg_0026_005fvalue_005fnobody.reuse(_jspx_th_i18n_005fmessageArg_005f0);
              _jspx_th_i18n_005fmessageArg_005f0_reused = true;
            } finally {
              org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessageArg_005f0, _jsp_annotationprocessor, _jspx_th_i18n_005fmessageArg_005f0_reused);
            }
            out.write('\n');
            out.write(' ');
            out.write(' ');
            //  i18n:messageArg
            org.apache.taglibs.i18n.MessageArgumentTag _jspx_th_i18n_005fmessageArg_005f1 = (org.apache.taglibs.i18n.MessageArgumentTag) _005fjspx_005ftagPool_005fi18n_005fmessageArg_0026_005fvalue_005fnobody.get(org.apache.taglibs.i18n.MessageArgumentTag.class);
            boolean _jspx_th_i18n_005fmessageArg_005f1_reused = false;
            try {
              _jspx_th_i18n_005fmessageArg_005f1.setPageContext(_jspx_page_context);
              _jspx_th_i18n_005fmessageArg_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_i18n_005fmessage_005f3);
              // /search.jsp(203,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_i18n_005fmessageArg_005f1.setValue(new Long(end));
              int _jspx_eval_i18n_005fmessageArg_005f1 = _jspx_th_i18n_005fmessageArg_005f1.doStartTag();
              if (_jspx_th_i18n_005fmessageArg_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                return;
              }
              _005fjspx_005ftagPool_005fi18n_005fmessageArg_0026_005fvalue_005fnobody.reuse(_jspx_th_i18n_005fmessageArg_005f1);
              _jspx_th_i18n_005fmessageArg_005f1_reused = true;
            } finally {
              org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessageArg_005f1, _jsp_annotationprocessor, _jspx_th_i18n_005fmessageArg_005f1_reused);
            }
            out.write('\n');
            out.write(' ');
            out.write(' ');
            //  i18n:messageArg
            org.apache.taglibs.i18n.MessageArgumentTag _jspx_th_i18n_005fmessageArg_005f2 = (org.apache.taglibs.i18n.MessageArgumentTag) _005fjspx_005ftagPool_005fi18n_005fmessageArg_0026_005fvalue_005fnobody.get(org.apache.taglibs.i18n.MessageArgumentTag.class);
            boolean _jspx_th_i18n_005fmessageArg_005f2_reused = false;
            try {
              _jspx_th_i18n_005fmessageArg_005f2.setPageContext(_jspx_page_context);
              _jspx_th_i18n_005fmessageArg_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_i18n_005fmessage_005f3);
              // /search.jsp(204,2) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_i18n_005fmessageArg_005f2.setValue(new Long(hits.getTotal()));
              int _jspx_eval_i18n_005fmessageArg_005f2 = _jspx_th_i18n_005fmessageArg_005f2.doStartTag();
              if (_jspx_th_i18n_005fmessageArg_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                return;
              }
              _005fjspx_005ftagPool_005fi18n_005fmessageArg_0026_005fvalue_005fnobody.reuse(_jspx_th_i18n_005fmessageArg_005f2);
              _jspx_th_i18n_005fmessageArg_005f2_reused = true;
            } finally {
              org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessageArg_005f2, _jsp_annotationprocessor, _jspx_th_i18n_005fmessageArg_005f2_reused);
            }
            out.write('\n');
            int evalDoAfterBody = _jspx_th_i18n_005fmessage_005f3.doAfterBody();
            if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
              break;
          } while (true);
          if (_jspx_eval_i18n_005fmessage_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
            out = _jspx_page_context.popBody();
          }
        }
        if (_jspx_th_i18n_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          return;
        }
        _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey.reuse(_jspx_th_i18n_005fmessage_005f3);
        _jspx_th_i18n_005fmessage_005f3_reused = true;
      } finally {
        org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessage_005f3, _jsp_annotationprocessor, _jspx_th_i18n_005fmessage_005f3_reused);
      }
      out.write('\n');
      out.write('\n');

// be responsive
out.flush();

      out.write("\n");
      out.write("\n");
      out.write("<br><br>\n");
      out.write("\n");
 if (clustering.equals("yes") && length != 0) { 
      out.write("\n");
      out.write("<table border=0 cellspacing=\"3\" cellpadding=\"0\">\n");
      out.write("\n");
      out.write("<tr>\n");
      out.write("\n");
      out.write("<td valign=\"top\">\n");
      out.write("\n");
 } 
      out.write('\n');
      out.write('\n');

  for (int i = 0; i < length; i++) {      // display the hits
    Hit hit = show[i];
    HitDetails detail = details[i];
    String title = detail.getValue("title");
    String url = detail.getValue("url");
    String id = "idx=" + hit.getIndexNo() + "&id=" + hit.getIndexDocNo();
    String summary = summaries[i].toHtml(true);
    String caching = detail.getValue("cache");
    boolean showSummary = true;
    boolean showCached = true;
    if (caching != null) {
      showSummary = !caching.equals(Nutch.CACHING_FORBIDDEN_ALL);
      showCached = !caching.equals(Nutch.CACHING_FORBIDDEN_NONE);
    }

    if (title == null || title.equals("")) {      // use url for docs w/o title
      title = url;
    }
    
      out.write("\n");
      out.write("    <b><a href=\"");
      out.print(url);
      out.write('"');
      out.write('>');
      out.print(Entities.encode(title));
      out.write("</a></b>\n");
      out.write("    ");
      out.write('\n');

    // @author John Xing
    // show meta info (currently type, size, date of last-modified)
    // for each hit. These info are indexed by ./src/plugin/index-more.

    // do not show unless we have something
    boolean showMore = false;

    // Content-Type
    String primaryType = detail.getValue("primaryType");
    String subType = detail.getValue("subType");

    String contentType = subType;
    if (contentType == null)
      contentType = primaryType;
    if (contentType != null) {
      contentType = "[<span class=\"contentType\">" + contentType + "</span>]";
      showMore = true;
    } else {
      contentType = "";
    }

    // Content-Length
    String contentLength = detail.getValue("contentLength");
    if (contentLength != null) {
      contentLength = "(" + contentLength + " bytes)";
      showMore = true;
    } else {
      contentLength = "";
    }

    // Last-Modified
    String lastModified = detail.getValue("lastModified");
    if (lastModified != null) {
      Calendar cal = new GregorianCalendar();
      cal.setTimeInMillis(new Long(lastModified).longValue());
      lastModified = cal.get(Calendar.YEAR)
                  + "." + (1+cal.get(Calendar.MONTH)) // it is 0-based
                  + "." + cal.get(Calendar.DAY_OF_MONTH);
      showMore = true;
    } else {
      lastModified = "";
    }

      out.write('\n');
      out.write('\n');
 if (showMore) {
    if ("text".equalsIgnoreCase(primaryType)) { 
      out.write("\n");
      out.write("    <br><font size=-1><nobr>");
      out.print(contentType);
      out.write(' ');
      out.print(contentLength);
      out.write(' ');
      out.print(lastModified);
      out.write("</nobr></font>\n");
  } else { 
      out.write("\n");
      out.write("    <br><font size=-1><nobr>");
      out.print(contentType);
      out.write(' ');
      out.print(contentLength);
      out.write(' ');
      out.print(lastModified);
      out.write(" - <a href=\"../text.jsp?");
      out.print(id);
      out.write('"');
      out.write('>');
      if (_jspx_meth_i18n_005fmessage_005f4(_jspx_page_context))
        return;
      out.write("</a></nobr></font>\n");
  }
  } 
      out.write('\n');
      out.write("\n");
      out.write("    ");
 if (!"".equals(summary) && showSummary) { 
      out.write("\n");
      out.write("    <br>");
      out.print(summary);
      out.write("\n");
      out.write("    ");
 } 
      out.write("\n");
      out.write("    <br>\n");
      out.write("    <span class=\"url\">");
      out.print(Entities.encode(url));
      out.write("</span>\n");
      out.write("    ");

      if (showCached) {
        
      out.write("(<a href=\"../cached.jsp?");
      out.print(id);
      out.write('"');
      out.write('>');
      if (_jspx_meth_i18n_005fmessage_005f5(_jspx_page_context))
        return;
      out.write("</a>) ");

    }
    
      out.write("\n");
      out.write("    (<a href=\"../explain.jsp?");
      out.print(id);
      out.write("&query=");
      out.print(URLEncoder.encode(queryString, "UTF-8"));
      out.write("&lang=");
      out.print(queryLang);
      out.write('"');
      out.write('>');
      if (_jspx_meth_i18n_005fmessage_005f6(_jspx_page_context))
        return;
      out.write("</a>)\n");
      out.write("    (<a href=\"../anchors.jsp?");
      out.print(id);
      out.write('"');
      out.write('>');
      if (_jspx_meth_i18n_005fmessage_005f7(_jspx_page_context))
        return;
      out.write("</a>)\n");
      out.write("    ");
 if (hit.moreFromDupExcluded()) {
    String more =
    "query="+URLEncoder.encode("site:"+hit.getDedupValue()+" "+queryString, "UTF8")
    +params+"&hitsPerSite="+0
    +"&lang="+queryLang
    +"&clustering="+clustering;
      out.write("\n");
      out.write("    (<a href=\"../search.jsp?");
      out.print(more);
      out.write('"');
      out.write('>');
      if (_jspx_meth_i18n_005fmessage_005f8(_jspx_page_context))
        return;
      out.write("\n");
      out.write("     ");
      out.print(hit.getDedupValue());
      out.write("</a>)\n");
      out.write("    ");
 } 
      out.write("\n");
      out.write("    <br><br>\n");
 } 
      out.write('\n');
      out.write('\n');
 if (clustering.equals("yes") && length != 0) { 
      out.write("\n");
      out.write("\n");
      out.write("</td>\n");
      out.write("\n");
      out.write("<!-- clusters -->\n");
      out.write("<td style=\"border-right: 1px dotted gray;\" />&#160;</td>\n");
      out.write("<td align=\"left\" valign=\"top\" width=\"25%\">\n");
      out.write('\n');


// @author Dawid Weiss
//
// PERFORMANCE/USER INTERFACE NOTE:
//
// What I do here is merely a demonstration. In real life the clustering
// process should be done in a separate "processing" stream, most likely
// a separate HTML frame that the user's browser requests data to.
// We don't want the user to wait with plain snippets until the clusters
// are created.
//
// Also: clustering is resource consuming, so a cache of recent queries 
// would be in place. Besides, such cache would also be beneficial for the
// purpose of re-querying existing clusters (remember that the
// clustering extension may be a heuristic returning a DIFFERENT set of
// clusters for an identical input).
// See www.vivisimo.com for details of how this can be done using frames, or
// http://carrot.cs.put.poznan.pl for an example of a Javascript solution.

// cluster the hits
HitsCluster [] clusters = null;
if (clusterer != null) {
  final long clusteringStart = System.currentTimeMillis();
  try {
    clusters = clusterer.clusterHits( details, Summary.toStrings(summaries) );
    final long clusteringDuration = System.currentTimeMillis() - clusteringStart;
    bean.LOG.info("Clustering took: " + clusteringDuration + " milliseconds.");
  } catch (Exception e) {
    // failed to do clustering (see below)
  }
}

if (clusterer == null) {
  
      out.write("No clustering extension found.");

} else {
  if (clusters == null) {
    
      out.write("Unable to do clustering.");

  } else if (clusters.length == 0) {
    
      out.write("No clusters found.");

  } else {
    // display top N clusters and top Q documents inside them.
    int N = 10;
    int Q = 3;
    int maxLabels = 2;
    
    int displayCounter = 0;
    N = Math.min(N, clusters.length );

    for (int clusterIndex = 0 ; clusterIndex < N ; clusterIndex++) {
      HitsCluster cluster = clusters[ clusterIndex ];
      String [] clusterLabels = cluster.getDescriptionLabels();
      
      // probably leave it on for now
      //if (cluster.isJunkCluster()) continue;

      // output cluster label.
      
      out.write("<div style=\"margin: 0px; padding: 0px; font-weight: bold;\">");

      for (int k=0;k<maxLabels && k<clusterLabels.length;k++) {
        if (k>0) out.print(", ");
        out.print( Entities.encode(clusterLabels[k]) );
      }
      
      out.write("</div>");

       
      // now output sample documents from the inside
      HitDetails[] documents = cluster.getHits();
      if (documents.length > 0) {
        
      out.write("<ul style=\"font-size: 90%; margin-top: .5em;\">");

        for (int k = 0; k < Q && k < documents.length; k++) {
          HitDetails detail = documents[ k ];
          String title = detail.getValue("title");
          String url = detail.getValue("url");
          if (title == null || title.equals("")) title = url;
          if (title.length() > 35) title = title.substring(0,35) + "...";
          
      out.write("\n");
      out.write("            <li><a href=\"");
      out.print(url);
      out.write('"');
      out.write('>');
      out.print( Entities.encode(title) );
      out.write("</a></li>\n");
      out.write("          ");

        }
        
      out.write("</ul>");

      }
       
      // ignore subclusters for now, ALTHOUGH HIERARCHICAL CLUSTERING
      // METHODS DO EXIST AND ARE VERY USEFUL
      // HitsCluster [] subclusters = cluster.getSubclusters();
    }
  }
}


      out.write('\n');
      out.write("\n");
      out.write("</td>\n");
      out.write("\n");
      out.write("</tr>\n");
      out.write("</table>\n");
      out.write("\n");
 } 
      out.write('\n');
      out.write('\n');


if ((hits.totalIsExact() && end < hits.getTotal()) // more hits to show
    || (!hits.totalIsExact() && (hits.getLength() > start+hitsPerPage))) {

      out.write("\n");
      out.write("    <form name=\"next\" action=\"../search.jsp\" method=\"get\">\n");
      out.write("    <input type=\"hidden\" name=\"query\" value=\"");
      out.print(htmlQueryString);
      out.write("\">\n");
      out.write("    <input type=\"hidden\" name=\"lang\" value=\"");
      out.print(queryLang);
      out.write("\">\n");
      out.write("    <input type=\"hidden\" name=\"start\" value=\"");
      out.print(end);
      out.write("\">\n");
      out.write("    <input type=\"hidden\" name=\"hitsPerPage\" value=\"");
      out.print(hitsPerPage);
      out.write("\">\n");
      out.write("    <input type=\"hidden\" name=\"hitsPerSite\" value=\"");
      out.print(hitsPerSite);
      out.write("\">\n");
      out.write("    <input type=\"hidden\" name=\"clustering\" value=\"");
      out.print(clustering);
      out.write("\">\n");
      out.write("    <input type=\"submit\" value=\"");
      if (_jspx_meth_i18n_005fmessage_005f9(_jspx_page_context))
        return;
      out.write('"');
      out.write('>');
      out.write('\n');
 if (sort != null) { 
      out.write("\n");
      out.write("    <input type=\"hidden\" name=\"sort\" value=\"");
      out.print(sort);
      out.write("\">\n");
      out.write("    <input type=\"hidden\" name=\"reverse\" value=\"");
      out.print(reverse);
      out.write('"');
      out.write('>');
      out.write('\n');
 } 
      out.write("\n");
      out.write("    </form>\n");

    }

if ((!hits.totalIsExact() && (hits.getLength() <= start+hitsPerPage))) {

      out.write("\n");
      out.write("    <form name=\"showAllHits\" action=\"../search.jsp\" method=\"get\">\n");
      out.write("    <input type=\"hidden\" name=\"query\" value=\"");
      out.print(htmlQueryString);
      out.write("\">\n");
      out.write("    <input type=\"hidden\" name=\"lang\" value=\"");
      out.print(queryLang);
      out.write("\">\n");
      out.write("    <input type=\"hidden\" name=\"hitsPerPage\" value=\"");
      out.print(hitsPerPage);
      out.write("\">\n");
      out.write("    <input type=\"hidden\" name=\"hitsPerSite\" value=\"0\">\n");
      out.write("    <input type=\"hidden\" name=\"clustering\" value=\"");
      out.print(clustering);
      out.write("\">\n");
      out.write("    <input type=\"submit\" value=\"");
      if (_jspx_meth_i18n_005fmessage_005f10(_jspx_page_context))
        return;
      out.write('"');
      out.write('>');
      out.write('\n');
 if (sort != null) { 
      out.write("\n");
      out.write("    <input type=\"hidden\" name=\"sort\" value=\"");
      out.print(sort);
      out.write("\">\n");
      out.write("    <input type=\"hidden\" name=\"reverse\" value=\"");
      out.print(reverse);
      out.write('"');
      out.write('>');
      out.write('\n');
 } 
      out.write("\n");
      out.write("    </form>\n");

    }

      out.write("\n");
      out.write("\n");
      out.write("<table bgcolor=\"3333ff\" align=\"right\">\n");
      out.write("<tr><td bgcolor=\"ff9900\"><a href=\"");
      out.print(rss);
      out.write("\"><font color=\"ffffff\"><b>RSS</b>\n");
      out.write("</font></a></td></tr>\n");
      out.write("</table>\n");
      out.write("\n");
      out.write("<p>\n");
      out.write("<a href=\"http://wiki.apache.org/nutch/FAQ\">\n");
      out.write("<img border=\"0\" src=\"../img/poweredbynutch_01.gif\">\n");
      out.write("</a>\n");
      out.write("\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/include/footer.html", out, false);
      out.write("\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else log(t.getMessage(), t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_i18n_005fbundle_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  i18n:bundle
    org.apache.taglibs.i18n.BundleTag _jspx_th_i18n_005fbundle_005f0 = (org.apache.taglibs.i18n.BundleTag) _005fjspx_005ftagPool_005fi18n_005fbundle_0026_005fbaseName_005fnobody.get(org.apache.taglibs.i18n.BundleTag.class);
    boolean _jspx_th_i18n_005fbundle_005f0_reused = false;
    try {
      _jspx_th_i18n_005fbundle_005f0.setPageContext(_jspx_page_context);
      _jspx_th_i18n_005fbundle_005f0.setParent(null);
      // /search.jsp(132,0) name = baseName type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_i18n_005fbundle_005f0.setBaseName("org.nutch.jsp.search");
      int _jspx_eval_i18n_005fbundle_005f0 = _jspx_th_i18n_005fbundle_005f0.doStartTag();
      if (_jspx_th_i18n_005fbundle_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
      _005fjspx_005ftagPool_005fi18n_005fbundle_0026_005fbaseName_005fnobody.reuse(_jspx_th_i18n_005fbundle_005f0);
      _jspx_th_i18n_005fbundle_005f0_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fbundle_005f0, _jsp_annotationprocessor, _jspx_th_i18n_005fbundle_005f0_reused);
    }
    return false;
  }

  private boolean _jspx_meth_i18n_005fmessage_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  i18n:message
    org.apache.taglibs.i18n.MessageTag _jspx_th_i18n_005fmessage_005f0 = (org.apache.taglibs.i18n.MessageTag) _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.i18n.MessageTag.class);
    boolean _jspx_th_i18n_005fmessage_005f0_reused = false;
    try {
      _jspx_th_i18n_005fmessage_005f0.setPageContext(_jspx_page_context);
      _jspx_th_i18n_005fmessage_005f0.setParent(null);
      // /search.jsp(136,14) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_i18n_005fmessage_005f0.setKey("title");
      int _jspx_eval_i18n_005fmessage_005f0 = _jspx_th_i18n_005fmessage_005f0.doStartTag();
      if (_jspx_th_i18n_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
      _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_i18n_005fmessage_005f0);
      _jspx_th_i18n_005fmessage_005f0_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessage_005f0, _jsp_annotationprocessor, _jspx_th_i18n_005fmessage_005f0_reused);
    }
    return false;
  }

  private boolean _jspx_meth_i18n_005fmessage_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  i18n:message
    org.apache.taglibs.i18n.MessageTag _jspx_th_i18n_005fmessage_005f1 = (org.apache.taglibs.i18n.MessageTag) _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.i18n.MessageTag.class);
    boolean _jspx_th_i18n_005fmessage_005f1_reused = false;
    try {
      _jspx_th_i18n_005fmessage_005f1.setPageContext(_jspx_page_context);
      _jspx_th_i18n_005fmessage_005f1.setParent(null);
      // /search.jsp(157,29) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_i18n_005fmessage_005f1.setKey("search");
      int _jspx_eval_i18n_005fmessage_005f1 = _jspx_th_i18n_005fmessage_005f1.doStartTag();
      if (_jspx_th_i18n_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
      _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_i18n_005fmessage_005f1);
      _jspx_th_i18n_005fmessage_005f1_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessage_005f1, _jsp_annotationprocessor, _jspx_th_i18n_005fmessage_005f1_reused);
    }
    return false;
  }

  private boolean _jspx_meth_i18n_005fmessage_005f2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  i18n:message
    org.apache.taglibs.i18n.MessageTag _jspx_th_i18n_005fmessage_005f2 = (org.apache.taglibs.i18n.MessageTag) _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.i18n.MessageTag.class);
    boolean _jspx_th_i18n_005fmessage_005f2_reused = false;
    try {
      _jspx_th_i18n_005fmessage_005f2.setPageContext(_jspx_page_context);
      _jspx_th_i18n_005fmessage_005f2.setParent(null);
      // /search.jsp(160,26) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_i18n_005fmessage_005f2.setKey("clustering");
      int _jspx_eval_i18n_005fmessage_005f2 = _jspx_th_i18n_005fmessage_005f2.doStartTag();
      if (_jspx_th_i18n_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
      _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_i18n_005fmessage_005f2);
      _jspx_th_i18n_005fmessage_005f2_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessage_005f2, _jsp_annotationprocessor, _jspx_th_i18n_005fmessage_005f2_reused);
    }
    return false;
  }

  private boolean _jspx_meth_i18n_005fmessage_005f4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  i18n:message
    org.apache.taglibs.i18n.MessageTag _jspx_th_i18n_005fmessage_005f4 = (org.apache.taglibs.i18n.MessageTag) _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.i18n.MessageTag.class);
    boolean _jspx_th_i18n_005fmessage_005f4_reused = false;
    try {
      _jspx_th_i18n_005fmessage_005f4.setPageContext(_jspx_page_context);
      _jspx_th_i18n_005fmessage_005f4.setParent(null);
      // /more.jsp(66,114) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_i18n_005fmessage_005f4.setKey("viewAsText");
      int _jspx_eval_i18n_005fmessage_005f4 = _jspx_th_i18n_005fmessage_005f4.doStartTag();
      if (_jspx_th_i18n_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
      _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_i18n_005fmessage_005f4);
      _jspx_th_i18n_005fmessage_005f4_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessage_005f4, _jsp_annotationprocessor, _jspx_th_i18n_005fmessage_005f4_reused);
    }
    return false;
  }

  private boolean _jspx_meth_i18n_005fmessage_005f5(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  i18n:message
    org.apache.taglibs.i18n.MessageTag _jspx_th_i18n_005fmessage_005f5 = (org.apache.taglibs.i18n.MessageTag) _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.i18n.MessageTag.class);
    boolean _jspx_th_i18n_005fmessage_005f5_reused = false;
    try {
      _jspx_th_i18n_005fmessage_005f5.setPageContext(_jspx_page_context);
      _jspx_th_i18n_005fmessage_005f5.setParent(null);
      // /search.jsp(252,43) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_i18n_005fmessage_005f5.setKey("cached");
      int _jspx_eval_i18n_005fmessage_005f5 = _jspx_th_i18n_005fmessage_005f5.doStartTag();
      if (_jspx_th_i18n_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
      _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_i18n_005fmessage_005f5);
      _jspx_th_i18n_005fmessage_005f5_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessage_005f5, _jsp_annotationprocessor, _jspx_th_i18n_005fmessage_005f5_reused);
    }
    return false;
  }

  private boolean _jspx_meth_i18n_005fmessage_005f6(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  i18n:message
    org.apache.taglibs.i18n.MessageTag _jspx_th_i18n_005fmessage_005f6 = (org.apache.taglibs.i18n.MessageTag) _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.i18n.MessageTag.class);
    boolean _jspx_th_i18n_005fmessage_005f6_reused = false;
    try {
      _jspx_th_i18n_005fmessage_005f6.setPageContext(_jspx_page_context);
      _jspx_th_i18n_005fmessage_005f6.setParent(null);
      // /search.jsp(255,109) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_i18n_005fmessage_005f6.setKey("explain");
      int _jspx_eval_i18n_005fmessage_005f6 = _jspx_th_i18n_005fmessage_005f6.doStartTag();
      if (_jspx_th_i18n_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
      _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_i18n_005fmessage_005f6);
      _jspx_th_i18n_005fmessage_005f6_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessage_005f6, _jsp_annotationprocessor, _jspx_th_i18n_005fmessage_005f6_reused);
    }
    return false;
  }

  private boolean _jspx_meth_i18n_005fmessage_005f7(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  i18n:message
    org.apache.taglibs.i18n.MessageTag _jspx_th_i18n_005fmessage_005f7 = (org.apache.taglibs.i18n.MessageTag) _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.i18n.MessageTag.class);
    boolean _jspx_th_i18n_005fmessage_005f7_reused = false;
    try {
      _jspx_th_i18n_005fmessage_005f7.setPageContext(_jspx_page_context);
      _jspx_th_i18n_005fmessage_005f7.setParent(null);
      // /search.jsp(256,38) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_i18n_005fmessage_005f7.setKey("anchors");
      int _jspx_eval_i18n_005fmessage_005f7 = _jspx_th_i18n_005fmessage_005f7.doStartTag();
      if (_jspx_th_i18n_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
      _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_i18n_005fmessage_005f7);
      _jspx_th_i18n_005fmessage_005f7_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessage_005f7, _jsp_annotationprocessor, _jspx_th_i18n_005fmessage_005f7_reused);
    }
    return false;
  }

  private boolean _jspx_meth_i18n_005fmessage_005f8(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  i18n:message
    org.apache.taglibs.i18n.MessageTag _jspx_th_i18n_005fmessage_005f8 = (org.apache.taglibs.i18n.MessageTag) _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.i18n.MessageTag.class);
    boolean _jspx_th_i18n_005fmessage_005f8_reused = false;
    try {
      _jspx_th_i18n_005fmessage_005f8.setPageContext(_jspx_page_context);
      _jspx_th_i18n_005fmessage_005f8.setParent(null);
      // /search.jsp(263,39) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_i18n_005fmessage_005f8.setKey("moreFrom");
      int _jspx_eval_i18n_005fmessage_005f8 = _jspx_th_i18n_005fmessage_005f8.doStartTag();
      if (_jspx_th_i18n_005fmessage_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
      _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_i18n_005fmessage_005f8);
      _jspx_th_i18n_005fmessage_005f8_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessage_005f8, _jsp_annotationprocessor, _jspx_th_i18n_005fmessage_005f8_reused);
    }
    return false;
  }

  private boolean _jspx_meth_i18n_005fmessage_005f9(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  i18n:message
    org.apache.taglibs.i18n.MessageTag _jspx_th_i18n_005fmessage_005f9 = (org.apache.taglibs.i18n.MessageTag) _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.i18n.MessageTag.class);
    boolean _jspx_th_i18n_005fmessage_005f9_reused = false;
    try {
      _jspx_th_i18n_005fmessage_005f9.setPageContext(_jspx_page_context);
      _jspx_th_i18n_005fmessage_005f9.setParent(null);
      // /search.jsp(296,32) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_i18n_005fmessage_005f9.setKey("next");
      int _jspx_eval_i18n_005fmessage_005f9 = _jspx_th_i18n_005fmessage_005f9.doStartTag();
      if (_jspx_th_i18n_005fmessage_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
      _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_i18n_005fmessage_005f9);
      _jspx_th_i18n_005fmessage_005f9_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessage_005f9, _jsp_annotationprocessor, _jspx_th_i18n_005fmessage_005f9_reused);
    }
    return false;
  }

  private boolean _jspx_meth_i18n_005fmessage_005f10(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  i18n:message
    org.apache.taglibs.i18n.MessageTag _jspx_th_i18n_005fmessage_005f10 = (org.apache.taglibs.i18n.MessageTag) _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.i18n.MessageTag.class);
    boolean _jspx_th_i18n_005fmessage_005f10_reused = false;
    try {
      _jspx_th_i18n_005fmessage_005f10.setPageContext(_jspx_page_context);
      _jspx_th_i18n_005fmessage_005f10.setParent(null);
      // /search.jsp(313,32) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_i18n_005fmessage_005f10.setKey("showAllHits");
      int _jspx_eval_i18n_005fmessage_005f10 = _jspx_th_i18n_005fmessage_005f10.doStartTag();
      if (_jspx_th_i18n_005fmessage_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
      _005fjspx_005ftagPool_005fi18n_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_i18n_005fmessage_005f10);
      _jspx_th_i18n_005fmessage_005f10_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_i18n_005fmessage_005f10, _jsp_annotationprocessor, _jspx_th_i18n_005fmessage_005f10_reused);
    }
    return false;
  }
}
