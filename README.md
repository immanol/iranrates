# Rates(Iran's exchange rates CLI)
This CLI is built based on Scalajs. It meant to be used as educational material for scala developers who want to start with Scalajs.
I use this CLI on daily bases to check the exchanges' rates from the console so I thought it might be useful for other developers as well! It is published as an NPM package as well and you can install and use it easily.
If you are a Scala developer that checks the exchange rates frequently :D, feel free to make a PR to improve the CLI!

# How to install
```shell
npm install -g iranrates
```
# How to use
```shell
Usage: rates [--exchange <name>] [--currency <name>]... [--output <output type>] [--timeout <miliseconds>]

Check the iran currency exchange rates

Options and flags:
    --help
        Display this help text.
    --version, -v
        Print the version number and exit.
    --exchange <name>, -e <name>
        The exchange to extract the rate. For example, [Bonbast,Tgju,All]
    --currency <name>, -c <name>
        The currency you look for its rate. For example, [All,EUR,USD,GBP,CHF,CAD,AUD,SEK,NOK,RUB,DKK,TRY]
    --output <output type>, -o <output type>
        Defines the type of output For example, [h, v, horizontal, vertical]. Default is Vertical
    --timeout <miliseconds>, -t <miliseconds>
        Defines the timeout for getting the information from each exchange. Default is 3000ms.     
```
For example;
```shell
iranrates -e bonbast -c eur -c usd
```
produce something like this;
```shell
             bonbast.com             
EUR           49,500                 
USD           46,550           
```
or 
```shell
iranrates -e bonbast -c all -c all
```
produce something like this;
```shell
              bonbast.com             tgju.org             
AUD           31,150                  30,920                 
CAD           33,950                  33,850                 
CHF           50,200                  50,170                 
DKK           6,645                   6,630                  
EUR           49,500                  49,226                 
GBP           56,450                  56,168                 
NOK           4,345                   4,330                  
RUB           605                     609                    
SEK           4,430                   4,410                  
TRY           2,450                   2,450                  
USD           46,550                  45,632                       
```

# Why Scalajs
It might sound weird to use Scalajs to develop a CLI! Scalajs compiles to javascript and the final output of a Scalajs project is a `js` file that can be used in a web application that targets the browsers or in an application that targets the Nodejs platform. So we can use Nodejs as a platform for our CLI.

# How to Start with Scalajs
To start with Scalajs for sure you need to know some Scala and a little bit of Javascript/Typescript and general understanding of the concept behind Nodejs. 
If you are targeting the browser you need to know some basics of Javascript or at least be able to read a piece of Javascript code. 
In general if you are targeting the Nodejs platform you need to know what is Nodejs and how it works.
I think just reading this [introduction](https://nodejs.dev/en/learn/introduction-to-nodejs/) would be enough!

## Scalajs
You can start a Scalajs project simply like any other Scala-Sbt project by defining your `build.sbt`. Check out the `build.sbt` from this project.
- The sbt plugin `sbt-scalajs` helps you to develop Scalajs and finally compile your Scalajs to Javascript.
- If you want to publish your projec as a NPM package you could use this plugin `sbt-npm-package`

Normally when you develop an application you would need some other capabilities on top of what Scala core provides you. A few of the main scala libraries already have been published for Scalajs which means you can use them directly in your Scalajs project. For example, `Cats`, `Cats-Effect`, `ZIO`, `FS2`, ... are already been published for Scalajs as well.
On top of these, you still might miss some functionalities in the Scala ecosystem. To cover those parts you could use the libraries that have already been developed in the Javascript ecosystem.

To use a javascript library in general you would need to introduce it to your project. There are a few ways to do that. In this project, we used the first one:
- Using the `sbt-scalajs-bundler` pluging.
- Using the `sbt-jsdependencies` pluging.
  
It is not enough to just introduce your javascript library to your project. You need to write a facade on top of the Javascript library.
This is the place the having some basic knowledge of Javascript could help. You can read all about this on the Scalajs [website](https://www.scala-js.org/doc/interoperability/facade-types.html).
For example, in this project, we wrote two facades one for `prompts` and one for `puppteer`.
Writing facades for big libraries could be hard and time-consuming, therefor we normally reduce the scope of the facade to the parts of the library that we use in the project.
The other option would be to use [ScalablyTyped](https://scalablytyped.org/docs/readme.html) which automatically converts the NPM packages to Scalajs for you. It has its own
pros and cons. I used it a few times and it works at least you can use it to have an idea of how to write your minimal facade by getting some inspiration from its output!

## How to run?
Like any other scala project you can use sbt to run your scalajs application. So you can use `sbt run` to run your application.<font color="red">In this project I had issue with `sbt run`. The cli prompts didn't work with sbt shell and I couldn't fix it! So I had to use nodejs directly to run my app.</font>

## How to run on local Nodejs?
You can use the commands below to make an npm package and install it locally:
This makes a NPM package in the project target folder. After this command you can go to the folder and just use `node .` to run the application.
```shell
npmPackage
```
This will install the npm package locally so you can run it directly. In this case you can just use `rates` to run the nodejs application.

If you want to publish it as a NPM package you first need to [create an account.](https://www.npmjs.com/) Then you need to login and then use sbt command to publish it.

```shell
npm login
sbt npmPackagePublish
```



