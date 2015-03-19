xquery version "1.0";

let $accession := request:get-parameter("accession", "")
let $root := xmldb:document("/db/ae/experiments.xml")

let $params :=
<parameters>
    <param name="host" value="localhost:8080"/>
    <param name="basepath" value="/ae"/>
</parameters>

return
    transform:stream-transform($root, "webapp:/WEB-INF/server-assets/stylesheets/browse-experiments-exist.xsl", $params)