xquery version "1.0";

(:
	Display all experiments and its similars.
:)

declare function local:get-similars($accession as xs:string) as element()?
{
    let $experiment := xmldb:document("/db/ae/similarity.xml")/similarity/experiment[self::node()/child::accession=$accession]
    return    
        $experiment/similarExperiments

};

declare function local:main() as element()*
{
    (:
        // all experiments
        let $experiments := xmldb:document("/db/ae/experiments.xml")//experiment

        //bounded for testing
    :)
    
    let $exps := xmldb:document("/db/ae/experiments.xml")//experiment
    let $experiments := $exps[position() < 20]

    for $experiment in $experiments
        let $acc := $experiment/accession/text()

        return
            element experiment
            {
                $experiment/*,
                local:get-similars($acc)
            }
};



let $sim :=
element experiments
{
    local:main()
}

(:
let $sim := xmldb:document("/db/ae/experiments.xml")
:)
return
    transform:stream-transform($sim, "webapp:/WEB-INF/server-assets/stylesheets/browse-experiments-similarity.xsl", ())
    
