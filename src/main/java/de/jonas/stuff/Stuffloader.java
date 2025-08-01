package de.jonas.stuff;

import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;

public class Stuffloader implements PluginLoader{

    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addRepository(new RemoteRepository.Builder(
            "MavenCentralLoad",
            "default",
            "https://repo1.maven.org/maven2/")
            .build());
        resolver.addDependency(new Dependency(
            new DefaultArtifact("dev.jorel:commandapi-bukkit-shade-mojang-mapped:10.1.2"),
            null));
        classpathBuilder.addLibrary(resolver);
    }
    
}
