#include <iostream>         // error handling and output
#include <cstdlib>          // EXIT_FAILURE

#include <GL/glew.h>        // GLEW library
#include "GLFW/glfw3.h"     // GLFW library

// GLM Math Header inclusions
#include <glm/glm.hpp>
#include <glm/gtx/transform.hpp>
#include <glm/gtc/type_ptr.hpp>

#include "SceneManager.h"
#include "ViewManager.h"
#include "ShapeMeshes.h"
#include "ShaderManager.h"

#define STB_IMAGE_IMPLEMENTATION
#include "../Utilities/stb_image.h"

// Namespace for declaring global variables
namespace
{
	// Macro for window title
	const char* const WINDOW_TITLE = "7-1 FinalProject and Milestones"; 

	// Main GLFW window
	GLFWwindow* g_Window = nullptr;

	// scene manager object for managing the 3D scene prepare and render
	SceneManager* g_SceneManager = nullptr;
	// shader manager object for dynamic interaction with the shader code
	ShaderManager* g_ShaderManager = nullptr;
	// view manager object for managing the 3D view setup and projection to 2D
	ViewManager* g_ViewManager = nullptr;
}

// Function declarations - all functions that are called manually
// need to be pre-declared at the beginning of the source code.
bool InitializeGLFW();
bool InitializeGLEW();

// Load a texture using stb_image

GLuint loadTexture(const char* filename) {
	GLuint textureID;
	glGenTextures(1, &textureID);

	int width, height, channels;
	unsigned char* data = stbi_load(filename, &width, &height, &channels, 0);
	if (data) {
		GLenum format = (channels == 3) ? GL_RGB : GL_RGBA;
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, data);
		glGenerateMipmap(GL_TEXTURE_2D);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	}
	else {
		std::cerr << "Failed to load texture: " << filename << std::endl;
	}

	stbi_image_free(data);
	return textureID;
}

/***********************************************************
 *  main(int, char*)
 *
 *  This function gets called after the application has been
 *  launched.
 ***********************************************************/
int main(int argc, char* argv[])
{
	// if GLFW fails initialization, then terminate the application
	if (InitializeGLFW() == false)
	{
		return(EXIT_FAILURE);
	}

	// try to create a new shader manager object
	g_ShaderManager = new ShaderManager();
	// try to create a new view manager object
	g_ViewManager = new ViewManager(
		g_ShaderManager);

	// try to create the main display window
	g_Window = g_ViewManager->CreateDisplayWindow(WINDOW_TITLE);

	// if GLEW fails initialization, then terminate the application
	if (InitializeGLEW() == false)
	{
		return(EXIT_FAILURE);
	}

	// load the shader code from the external GLSL files
	g_ShaderManager->LoadShaders(
		"../../Utilities/shaders/vertexShader.glsl",
		"../../Utilities/shaders/fragmentShader.glsl");
	g_ShaderManager->use();

	// Load the texture and store it globally
	GLuint myTexture = loadTexture("../../Utilities/Textures/rusticwood.png");


	// === Set Phong Lighting Uniforms ===
	// Lighting and texture uniforms
	GLuint shaderID = g_ShaderManager->getShaderProgramID();
	glUniform1i(glGetUniformLocation(shaderID, "bUseLighting"), GL_TRUE);
	glUniform1i(glGetUniformLocation(shaderID, "bUseTexture"), GL_TRUE);

	glUniform3f(glGetUniformLocation(shaderID, "viewPosition"), 0.0f, 0.0f, 5.0f);
	glUniform2f(glGetUniformLocation(shaderID, "UVscale"), 1.0f, 1.0f);
	glUniform4f(glGetUniformLocation(shaderID, "objectColor"), 1.0f, 1.0f, 1.0f, 1.0f);

	glUniform3f(glGetUniformLocation(shaderID, "lightSources[0].position"), 1.0f, 2.0f, 2.0f);
	glUniform3f(glGetUniformLocation(shaderID, "lightSources[0].ambientColor"), 0.2f, 0.2f, 0.2f);
	glUniform3f(glGetUniformLocation(shaderID, "lightSources[0].diffuseColor"), 1.0f, 1.0f, 1.0f);
	glUniform3f(glGetUniformLocation(shaderID, "lightSources[0].specularColor"), 1.0f, 1.0f, 1.0f);
	glUniform1f(glGetUniformLocation(shaderID, "lightSources[0].focalStrength"), 32.0f);
	glUniform1f(glGetUniformLocation(shaderID, "lightSources[0].specularIntensity"), 0.5f);

	glUniform3f(glGetUniformLocation(shaderID, "material.ambientColor"), 1.0f, 1.0f, 1.0f);
	glUniform1f(glGetUniformLocation(shaderID, "material.ambientStrength"), 0.1f);
	glUniform3f(glGetUniformLocation(shaderID, "material.diffuseColor"), 1.0f, 1.0f, 1.0f);
	glUniform3f(glGetUniformLocation(shaderID, "material.specularColor"), 1.0f, 1.0f, 1.0f);
	glUniform1f(glGetUniformLocation(shaderID, "material.shininess"), 16.0f);


	// try to create a new scene manager object and prepare the 3D scene
	g_SceneManager = new SceneManager(g_ShaderManager);
	g_SceneManager->PrepareScene();

	// loop will keep running until the application is closed 
	// or until an error has occurred
	while (!glfwWindowShouldClose(g_Window))
	{
		// Enable z-depth
		glEnable(GL_DEPTH_TEST);

		// Clear the frame and z buffers
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// convert from 3D object space to 2D view
		g_ViewManager->PrepareSceneView();

		// refresh the 3D scene
		glBindTexture(GL_TEXTURE_2D, myTexture);
		g_SceneManager->RenderScene();


		// Flips the the back buffer with the front buffer every frame.
		glfwSwapBuffers(g_Window);

		// query the latest GLFW events
		glfwPollEvents();
	}

	// clear the allocated manager objects from memory
	if (NULL != g_SceneManager)
	{
		delete g_SceneManager;
		g_SceneManager = NULL;
	}
	if (NULL != g_ViewManager)
	{
		delete g_ViewManager;
		g_ViewManager = NULL;
	}
	if (NULL != g_ShaderManager)
	{
		delete g_ShaderManager;
		g_ShaderManager = NULL;
	}

	// Terminates the program successfully
	exit(EXIT_SUCCESS); 
}

/***********************************************************
 *	InitializeGLFW()
 * 
 *  This function is used to initialize the GLFW library.   
 ***********************************************************/
bool InitializeGLFW()
{
	// GLFW: initialize and configure library
	// --------------------------------------
	glfwInit();

#ifdef __APPLE__
	// set the version of OpenGL and profile to use
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
#else
	// set the version of OpenGL and profile to use
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
#endif
	// GLFW: end -------------------------------

	return(true);
}

/***********************************************************
 *	InitializeGLEW()
 *
 *  This function is used to initialize the GLEW library.
 ***********************************************************/
bool InitializeGLEW()
{
	// GLEW: initialize
	// -----------------------------------------
	GLenum GLEWInitResult = GLEW_OK;

	// try to initialize the GLEW library
	GLEWInitResult = glewInit();
	if (GLEW_OK != GLEWInitResult)
	{
		std::cerr << glewGetErrorString(GLEWInitResult) << std::endl;
		return false;
	}
	// GLEW: end -------------------------------

	// Displays a successful OpenGL initialization message
	std::cout << "INFO: OpenGL Successfully Initialized\n";
	std::cout << "INFO: OpenGL Version: " << glGetString(GL_VERSION) << "\n" << std::endl;

	return(true);
}
