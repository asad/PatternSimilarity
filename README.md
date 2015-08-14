Pattern Similarity
=================

Generic code for reporting Tanimoto similarity between two pattern vectors with weights

1) Option with predefined patterns (-p) with binary similarity
> java -jar dist/PatternSimilarity.jar -f CSV -q data/file1.fp -t data/file2.fp -p data/pattern.fp -B
> Tanimoto: 1.00

2) Option with predefined patterns (-p) with weighted similarity
>java -jar dist/PatternSimilarity.jar -f CSV -q data/file1.fp -t data/file2.fp -p data/pattern.fp 
>Tanimoto: 0.75


3) Option with binary similarity
> java -jar dist/PatternSimilarity.jar -f CSV -q data/file1.fp -t data/file2.fp -B
> Tanimoto: 0.38

4) Option with weighted similarity
>java -jar dist/PatternSimilarity.jar -f CSV -q data/file1.fp -t data/file2.fp 
>Tanimoto: 0.40
