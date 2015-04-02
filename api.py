from flask import Flask, request
import time
app = Flask(__name__)

# jekyll post params
LB = '\n'
Seperator = '---' + LB
Layout = 'layout: '
Title = 'title: '
Category = 'categories: '
Tags = 'tags: '
Excerpt = 'excerpt: '

# Fixing layout to post
Post = Seperator + Layout + 'post' + LB

@app.route('/', methods=['POST', 'GET'])
def publish_post():
	nPost = Post
	nTitle = time.strftime("%Y-%m-%d") + '-Log_' + time.strftime("%B-%d") + '.md'

	# Title
	try:
		nPost += Title + request.form['title'] + LB
		nTitle = time.strftime("%Y-%m-%d") + '-' + request.form['title'].replace(' ', '_') + '.md'
	except KeyError:
		nPost += Title + "Log " + time.strftime("%B-%d") + LB

	# Categories
	try:
		nPost += Category + request.form['cats'] + LB
	except KeyError:
		nPost += Category + 'Log ' + LB

	# Tags
	try:
		nPost += Tags + request.form['tags'] + LB
	except KeyError:
		print 'no tags set'

	# Excerpt
	try:
		nPost += Excerpt + request.form['excerpt'] + LB
	except KeyError:
		nPost += Excerpt + 'Log for ' + time.strftime("%d-%m-%Y") + LB

	# Close header section
	nPost += LB + Seperator + LB

	# Content
	try:
		nPost += request.form['content'] + LB
	except KeyError:
		return 'No content sent. Aborted!'

	# Write to new post file
	nFile = open('../jekyll/_posts/' + nTitle, 'w')
	nFile.write(nPost)
	nFile.close()

	print nPost

	return nPost

if __name__ == '__main__':
	app.debug = True
	app.run(host='0.0.0.0', port=2525)
