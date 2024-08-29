<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- apiOverview.jsp -->
<div class="gradientbox" style="width:90%;">
  <dl>
    <dt>
      <h1>InterMine API</h1>
    </dt>
    <dd>
      <div>
        <p>An API is available to programmatically access ${WEB_PROPERTIES['project.title']}. Perl, Python, Ruby, and Java are the languages supported by the InterMine API. Use the menu above to view details on using the client libraries for each language.</p>
        <p>Additionally, some Python examples using the ${WEB_PROPERTIES['project.title']} API are available in the <a href="https://github.com/elsiklab/intermine-api-python-examples/tree/main/${WEB_PROPERTIES['webapp.path']}">intermine-api-python-examples</a> repository on GitHub.</p>
      </div>
    </dd>
  </dl>
</div>
<!-- /apiOverview.jsp -->
