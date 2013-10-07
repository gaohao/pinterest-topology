pintr
=======
1. Install maven3 first  

2. Install virtualenv  
<pre>pip install virtualenv</pre>  

3. Install and initial an virtualenv  
<pre>
cd vinig-repo  
virtualenv venv --distribute  
source venv/bin/activate
</pre>

4. Install all python dependency  
<pre>pip install -r requirements.txt</pre>

5. Run the topology  
<pre>
mvn -f pom.xml compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=me.haogao.pintr.topology.PintrTopology  
</pre>

6. Query
  <pre>
  zrevrange pintr:orig:host:animals:rank 0 9 withscores  
  </pre>

  <pre>
  smembers pintr:orig:host:animals:www.flickr.com  
  </pre>
  
  <pre>
  scard pintr:pins:ids:animals
  </pre>
