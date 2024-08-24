# CompAI

Compliance checKing for privacy policies using AI (CompAI) is a tool, associated with the paper titled: "AI-enabled Automation for Completeness 
Checking of Privacy Policies". CompAI leverages Machine Learning (ML) and Natural Language Processing (NLP) to analyze Privacy Policies (PPs) 
to verify its compliance against the General Data Protection Regulation (GDPR).

This README file contains the details on how to use CompAI, where to
find it, and how to interpret the results obtained by CompAI.

## What is released?
- ./CompAI.zip: contains the source code of the Maven project, and an executable jar file (compai.jar) for running our implementation.
- ./EvaluationSet: contains the non-proprietary PPs which we use in our work. We provide both the original PPs as well as the annotated ones.
- ./WebService.zip: contains the source of the project as a web service. Instructions about using CompAI as a web service are provided below.

## How to use CompAI?

- Acces CompAI as a web service through: https://compai.uni.lu

- login by specifying username and password. You can contact us to obtain your own credentials.

- Choose the privacy policy to be analyzed and submit. The permited formats are .doc, .docx, and .pdf.

- Answer the questionnaire and submit the responses. The performance of the tool depends on the responses to these questions.

*Note** that waiting for the analysis of the privacy policy might take some minutes. 

*Note** that the privacy policy you wish to analyze can be located anywhere in your local machine.

## Output of CompAI

Once the execution is successfully completed, it will appear in the screen all the details regarding the compliance checking details.
The results will show three tabs. Tab1 shows the violated and satisfied criteria, and the rational behind evry decision made.
Tab2 shows more details for each compliance checking criteria. Tab3 is for downloading the report to your local machine.

*Note** that CompAI generates the report file named "PP_NAME_report", that contains the answers to the questionnaire, the details shown 
in Tab1 and Tab2, and a glossary of terms in case is needed to better understand the context.

