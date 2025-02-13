SAMPLES_DIR=./samples
rm -rf $SAMPLES_DIR
mkdir $SAMPLES_DIR
GENERATED=../../../../../target/parser/metamodel/test-output/projectBuild
for i in $GENERATED/*
do
	cp -r $i/src $SAMPLES_DIR/${i##*/}
done
find $SAMPLES_DIR -name \*.java -exec rename -s .java .j {} \;
