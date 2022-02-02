# VirusAnaliser
User manual for CovAch (COVIDs Achilles' heel) software

## The functionality of this software

The software is designed to detect genetically conserved regions within viral genomes. For the software to work correctly, you need to write your own parser that allows you to convert files of annotated genomic sequences of SARS-CoV-2 or another virus into a json file format that is readable by the program.

The program is adapted mainly for the detection of short conservative regions of the genome (about 100 bp) and is focused primarily on the search for vulnerabilities in the virus genome suitable for the creation of therapeutic agents based on siRNA.

The software is optimized for the analysis of genomes with a length from 15 to 80 thousand nucleotides, however, if the internal parameters of the program are changed (not available to the ordinary user), it is possible to achieve greater analysis efficiency for genomes of different lengths. For more information, you can contact the developer.

## How to use the software

For the software to work correctly, you need to install it in a folder whose path contains only Latin characters.

For analysis by the program, genomic sequencing data must be prepared in a certain way:

- the sequences of viral genomes should be in a folder whose path consists only of Latin characters

- there should be no other files in the folder with viral genomes

- each genome should be stored in a separate json file in the following format:

{"dataset": "datasetexample", "sequence": "sequence Example"}

The dataset parameter specifies the unique identifier of the SARS-CoV-2 genomic DNA sequence, the "sequence" parameter specifies the cDNA sequence complementary to the SARS-CoV-2 genomic RNA. The "sequence" parameter can contain only symbols used in international practice to denote nucleotides: a g c t n

As an example, datasetExample = "mw636399", sequenceExample = "agcgcgcgcgcgct"

Software settings that can be configured by the user

The software user can configure the following parameters:

- the folder from which the genomes will be taken

- output folder

- the percentage of deviation of genome mutations from the maximum found

(if we found a genome that does not mutate in 95% of cases, then we also consider genomes that do not mutate in more than 95%-k% of cases)

- choosing whether to use genomes with an incomplete genome (with a genome containing n)

## Program output:

The program creates a folder in which, upon completion of the work, the file "results.json" will appear, in which the result will be presented in the following format:

{

"defininglength": "40",

- the length of the minimum "unique for the program" piece

"scatterInResults": "3",

- the maximum percentage of deviation from the most non-mutated piece

"numberOfGenomes": "104",

- the number of genomes found in the folder provided by the user

"Numberofngenomes": "86",

- the number of unexplored genomes found

"usengenomes": "false",

- whether the program used unexplored genomes in the analysis

"numberofanalysedgenomes": "18",

- the number of genomes that the program examined

"genomes": [

{

"minQuantity": "18",

- the minimum number of genomes in which there is a given sequence

"maxQuantity": "18",

- the maximum number of genomes in which there is the sequence

"length": "159",

- the length of the genome

"sequence": "t...ttc"

- Sam gene was found

},

...

{

"minQuantity": "18",

"maxQuantity": "18",

"length": "41",

"sequence": "gaaacattttacccaaaattacaatctagtcaagcgtggca"

},

]

}
