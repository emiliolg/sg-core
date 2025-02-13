<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xml:lang='en' xmlns='http://www.w3.org/1999/xhtml' lang='en'>
    <head>
        <title>$title</title>

        <link rel="icon" href="$csspath/favicon.png">
        <link rel="stylesheet" href="$csspath/gfm.css">
        <link rel="stylesheet" type="text/css" href="$csspath/tipuesearch.css">

        <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.0.0/styles/idea.min.css">
        <script src="http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.0.0/highlight.min.js"></script>
        <script>hljs.initHighlightingOnLoad();</script>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

        <!-- Optional theme -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

        <!-- Latest compiled and minified JavaScript -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>


    </head>
    <body>
    <nav class="navbar navbar-default navbar-static-top">
          <div class="container">
            <div class="navbar-header">

              <a href="http://www.tekgenesis.com"><img src="$homepath/logo-tek.png" alt="TekGenesis" /> </a>

            </div>
            <div id="navbar" class="navbar-collapse collapse">
              <!-- ul class="nav navbar-nav"-->
              <ul class="nav navbar-nav navbar-right">
<form action="$homepath/tipuesearch/search.html">
<div class="tipue_search_right"><input type="text" name="q" id="tipue_search_input" pattern=".{3,}" title="At least 3 characters" required></div>
<div class="tipue_search_left"><img src="$homepath/tipuesearch/search.png" class="tipue_search_icon"></div>
<div style="clear: both;"></div>
</form>

              </ul>
              <ul class="nav navbar-nav navbar-right">
                <li><a href="$homepath/index.html">Documentation Home</a></li>
              </ul>
            </div><!--/.nav-collapse -->
          </div>
        </nav>
        <div class="container bs-docs-container">
    <div class="row">
      <!--div class="col-md-9" role="main"-->
      <div class="$contentClass" role="main">
      <article class="markdown-body">
        $content
      </article>
</div>
    <!--div class="col-md-3" role="complementary"> <nav class="bs-docs-sidebar hidden-print hidden-xs hidden-sm affix-top"-->
    <div class="$navigatorClass" role="complementary"> <nav class="bs-docs-sidebar hidden-print hidden-xs hidden-sm affix-top">
    <article class="markdown-body"> $indexContent
      </article><ul class="nav">
      <li class=""></li>  </ul>  </nav> </div>
  </div>
</div>
    </body>
</html>